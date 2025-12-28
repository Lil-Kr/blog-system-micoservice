package org.cy.micoservice.blog.message.provider.mq.rmqconsumer;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultMessageDTO;
import org.cy.micoservice.blog.audit.facade.enums.AuditResultCodeEnum;
import org.cy.micoservice.blog.common.exception.BizException;
import org.cy.micoservice.blog.entity.message.model.provider.po.ChatBoxEs;
import org.cy.micoservice.blog.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.blog.im.facade.dto.connector.ImMessageDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatContentDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReqDTO;
import org.cy.micoservice.blog.message.facade.enums.ChatMsgStatusEnum;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.cy.micoservice.blog.message.provider.config.MessageCacheKeyBuilder;
import org.cy.micoservice.blog.message.provider.constant.MessageChatRelationConstants;
import org.cy.micoservice.blog.message.provider.service.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 接收im业务消息的消费者, 注意由于之前im是根据uri去区分业务场景的, 所以不同topic的消息格式都是隔离的
 */
@Slf4j
@Component
public class ImBizMessageConsumer implements InitializingBean {

  @Autowired
  private RocketMQConsumerProperties rocketMQConsumerProperties;
  @Autowired
  private MessageApplicationProperties messageApplicationProperties;
  @Autowired
  private ImMessageService imMessageService;
  @Autowired
  private ChatRelationEsService chatRelationEsService;
  @Autowired
  private ImPushService imPushService;
  @Autowired
  private ChatRecordEsService chatRecordEsService;
  @Autowired
  private ChatBoxEsService chatBoxEsService;
  @Autowired
  private MessageCacheKeyBuilder messageCacheKeyBuilder;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;

  @Override
  public void afterPropertiesSet() {
    Thread consumeMqTask = new Thread(() -> {
      try {
        this.initImBizMessageConsumer();
      } catch (Exception e) {
        log.error("consume im msg error:", e);
        throw new RuntimeException(e);
      }
    });
    consumeMqTask.setName("im-msg-consume-task");
    consumeMqTask.start();
  }

  /**
   * 初始化业务消息消费者
   * @throws MQClientException
   */
  private void initImBizMessageConsumer() throws MQClientException {
    DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
    mqPushConsumer.setVipChannelEnabled(false);
    mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameserver());
    mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroup() + "_" + ImBizMessageConsumer.class.getSimpleName());
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.subscribe(messageApplicationProperties.getImBizMessageConsumerTopic(), "");
    mqPushConsumer.setConsumeMessageBatchMaxSize(messageApplicationProperties.getImBizMessageConsumerBatchSize());
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
        List<ImChatReqDTO> imChatReqDTOList = this.doMsgAuditAsyncHandle(messages);
        //增加消息总数, offset值
        this.incrMsgCountOffset(imChatReqDTOList);
        // 批量保存
        this.syncToEs(imChatReqDTOList);
        // 推送消息到router层处理
        this.batchPushToRoute(imChatReqDTOList);
      } catch (Exception e) {
        log.error("consumer message has error:", e);
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    mqPushConsumer.start();
    log.info("ImBizMessageConsumer started!");
  }

  /**
   * 增加消息总数offset
   * @param imChatReqDTOList
   */
  private void incrMsgCountOffset(List<ImChatReqDTO> imChatReqDTOList) {
    // todo 后续可以优化, 统计senderId, 减少redis层的io访问
    // 如果cache没有数据, 只能从es层查询, 但是这部分的量会较少一些
    imChatReqDTOList.parallelStream()
      .forEach(imChatReq -> {
        String cacheKey = messageCacheKeyBuilder.chatRelationMsgCountKey(imChatReq.getSenderId());
        long number = redisTemplate.opsForHash().increment(cacheKey, imChatReq.getRelationId(), 1);
        imChatReq.setSeqNo(number);
      });
  }

  /**
   * todo: 有3个位置对ES写入, 优化 -> 使用多线程机制, 异步写入到ES
   * 批量保存到ES
   * @param imChatReqDTOList
   */
  private void syncToEs(List<ImChatReqDTO> imChatReqDTOList) {
    // 批量保存消息记录
    chatRecordEsService.bulk(imChatReqDTOList);
    // 更新relation关系
    Map<String, ImChatReqDTO> imChatReqDTOMap = this.getLatestChatByRelationId(imChatReqDTOList);

    List<ChatRelationReqDTO> chatRelationReqDTOList = new ArrayList<>();
    List<ChatBoxEs> chatBoxEsList = new ArrayList<>();
    for (String relationId : imChatReqDTOMap.keySet()) {
      ImChatReqDTO imChatReqDTO = imChatReqDTOMap.get(relationId);
      ImChatContentDTO imChatContentDTO = JSON.parseObject(imChatReqDTO.getContent(), ImChatContentDTO.class);
      ChatRelationReqDTO relationReqDTO = new ChatRelationReqDTO();
      relationReqDTO.setRelationId(relationId);
      relationReqDTO.setId(relationId);
      relationReqDTO.setUserId(imChatReqDTO.getSenderId());
      relationReqDTO.setReceiverId(imChatReqDTO.getReceiverId());
      relationReqDTO.setSeqNo(imChatReqDTO.getSeqNo());
      relationReqDTO.setContent(imChatContentDTO.getBody());
      relationReqDTO.setType(imChatContentDTO.getType());
      chatRelationReqDTOList.add(relationReqDTO);

      // 收信箱
      ChatBoxEs chatBoxEs = ChatBoxEs.builder()
        .id(String.format(MessageChatRelationConstants.RELATION_ID_FORMAT, relationId, imChatReqDTO.getSenderId()))
        .userId(imChatReqDTO.getSenderId())
        .relationId(relationId)
        // 更新发送人的offset
        .msgOffset(imChatReqDTO.getSeqNo())
        .updateTime(System.nanoTime())
        .build();
      chatBoxEsList.add(chatBoxEs);
    }
    // 批量保存会话关系记录
    chatRelationEsService.bulk(chatRelationReqDTOList);

    // 批量保存发信人的收件箱位置
    chatBoxEsService.bulk(chatBoxEsList);
  }

  /**
   * 异步化审核处理链路
   * @param messages
   * @return
   */
  private List<ImChatReqDTO> doMsgAuditAsyncHandle(List<MessageExt> messages) {
    List<ImChatReqDTO> imChatReqDTOList = new CopyOnWriteArrayList<>();
    // 使用并行提升速率
    messages.parallelStream().forEach(msg -> {
      ImChatReqDTO imChatReqDTO = this.generateImChatReqDTO(msg);
      // 审核这里需根据内容决定走不同类型的判断/视频, 图片, 音频, 文字的审核耗时不太一样
      this.doMsgAuditHandle(imChatReqDTO);
      if (Optional.ofNullable(imChatReqDTO).map(ImChatReqDTO::getMsgStatus).isEmpty()) {
        log.error("invalid msg dto: {}", JSON.toJSONString(imChatReqDTO));
      } else {
        imChatReqDTOList.add(imChatReqDTO);
      }
    });
    return imChatReqDTOList;
  }

  /**
   * 处理审核逻辑
   * @param imChatReqDTO
   */
  private void doMsgAuditHandle(ImChatReqDTO imChatReqDTO) {
    if (Objects.isNull(imChatReqDTO)) return;

    if (imMessageService.isTextMessage(imChatReqDTO)) {
      boolean auditStatus = this.doTextAudit(imChatReqDTO);
      if (! auditStatus) {
        //审核失败 需要返回失败通知给到发送人
        imChatReqDTO.setReceiverId(imChatReqDTO.getSenderId());
        imChatReqDTO.setMsgStatus(ChatMsgStatusEnum.INVALID_BODY.getCode());
        return;
      }
      imChatReqDTO.setMsgStatus(ChatMsgStatusEnum.SUCCESS.getCode());
    } else if (imMessageService.isImageMessage(imChatReqDTO)) {
      boolean sendStatus = this.doImageAudit(imChatReqDTO);
      if (! sendStatus) {
        //审核失败 需要返回失败通知给到发送人
        imChatReqDTO.setReceiverId(imChatReqDTO.getSenderId());
        imChatReqDTO.setMsgStatus(ChatMsgStatusEnum.SYSTEM_ERROR.getCode());
        return;
      }
      imChatReqDTO.setMsgId(ChatMsgStatusEnum.WAITING_CHECK_RESP.getDesc());
    }
  }

  /**
   * 批量推送给im的 router 层
   * @param imChatReqDTOList
   */
  private void batchPushToRoute(List<ImChatReqDTO> imChatReqDTOList) {
    List<ImChatReqDTO> successMsgList = imChatReqDTOList.stream()
      .filter(chatReq -> chatReq.getMsgStatus().equals(ChatMsgStatusEnum.SUCCESS.getCode()))
      .collect(Collectors.toList());
    imPushService.batchPushRouterMessage(successMsgList);
  }

  /**
   * 执行文本审核, 走rpc调用
   * @param imChatReqDTO
   * @return
   */
  private boolean doTextAudit(ImChatReqDTO imChatReqDTO) {
    try {
      // 文本消息可以尝试走rpc调用
      AuditResultMessageDTO auditResult = imMessageService.getTextAuditMessageResult(imChatReqDTO);
      return AuditResultCodeEnum.VALID.getCode().equals(auditResult.getCode());
    } catch (BizException e) {
      log.error("text audit error: ", e);
    }
    return false;
  }

  /**
   * 执行图片审核, 发送到 mq
   * @param imChatReqDTO
   * @return
   */
  private boolean doImageAudit(ImChatReqDTO imChatReqDTO) {
    boolean sendStatus = imMessageService.sendAuditMessageToMQ(imChatReqDTO);
    if (!sendStatus) {
      log.error("send audit mq failed");
    }
    return sendStatus;
  }

  /**
   * 生成ImChatReqDTO对象
   * @param msg
   * @return
   */
  private ImChatReqDTO generateImChatReqDTO(MessageExt msg) {
    String imMessageBodyJson = new String(msg.getBody());
    try {
      ImMessageDTO imMessageDTO = JSON.parseObject(imMessageBodyJson, ImMessageDTO.class);
      String imBody = imMessageDTO.getBody();

      // 提前解析对象格式, 同时注入消息id
      ImChatReqDTO imChatReqDTO = JSON.parseObject(imBody, ImChatReqDTO.class);
      imChatReqDTO.setMsgId(UUID.randomUUID().toString());
      imChatReqDTO.setTraceId(imMessageDTO.getTraceId());
      imChatReqDTO.setSenderId(imMessageDTO.getSenderId());
      return imChatReqDTO;
    } catch (Exception e) {
      log.error("invalid msg body, msg is: {}", imMessageBodyJson);
    }
    return null;
  }

  /**
   * 方法: 按relationId分组, 获取每个分组seqNo最新的记录
   * @param imChatReqDTOList
   * @return
   */
  public Map<String, ImChatReqDTO> getLatestChatByRelationId(List<ImChatReqDTO> imChatReqDTOList) {
    return imChatReqDTOList.stream()
      .collect(Collectors.groupingBy(
        // 分组key: 获取当前 ImChatReqDTO 对应的 relationId (需根据实际业务补充获取逻辑)
        ImChatReqDTO::getRelationId,
        // 下游收集器: 筛选每个分组内seqNo最新的记录
        Collectors.collectingAndThen(
          Collectors.toList(), list ->
            // 按seqNo降序排序
            // 获取排序后的第一条 (最新记录), 无数据时返回null
            list.stream().max(Comparator.comparing(ImChatReqDTO::getSeqNo)).orElse(null)
        )
      ));
  }
}