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
import org.cy.micoservice.blog.common.constants.CommonFormatConstants;
import org.cy.micoservice.blog.common.exception.BizException;
import org.cy.micoservice.blog.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.blog.im.facade.dto.connector.ImMessageDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReqDTO;
import org.cy.micoservice.blog.message.facade.enums.ChatMsgStatusEnum;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.cy.micoservice.blog.message.provider.config.MessageCacheKeyBuilder;
import org.cy.micoservice.blog.message.provider.config.async.ChatMessageAsyncTaskSubmitter;
import org.cy.micoservice.blog.message.provider.service.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
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
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  private ChatMessageAsyncTaskSubmitter chatMessageAsyncTaskSubmitter;

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
    consumeMqTask.setName("im-biz-msg-consume-task");
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
    mqPushConsumer.setConsumerGroup(String.format(CommonFormatConstants.COMMENT_FORMAT_UNDERSCORE_SPLIT, rocketMQConsumerProperties.getGroup(), ImBizMessageConsumer.class.getSimpleName()));
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.subscribe(messageApplicationProperties.getImBizMessageConsumerTopic(), "");
    mqPushConsumer.setConsumeMessageBatchMaxSize(messageApplicationProperties.getImBizMessageConsumerBatchSize());
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
        List<ImChatReqDTO> imChatReqDTOList = this.doMsgAuditAsyncHandler(messages);
        // 增加消息总数, offset值
        this.incrMsgCountOffset(imChatReqDTOList);
        // 批量保存
        this.syncToEs(imChatReqDTOList);
        // 推送消息到router层处理
        this.batchPushToRoute(imChatReqDTOList);
      } catch (Exception e) {
        log.error("consumer message has error: ", e);
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    mqPushConsumer.start();
    log.info("ImBizMessageConsumer started!");
  }

  /**
   * 增加消息总数offset: redis
   * @param imChatReqDTOList
   */
  private void incrMsgCountOffset(List<ImChatReqDTO> imChatReqDTOList) {
    /**
     * chat_relation_id -> List<ImChatReqDTO>
     * Map<String, List<ImChatReqDTO>> relationGroupMap
     */
    imChatReqDTOList.stream()
      .collect(Collectors.groupingBy(ImChatReqDTO::getRelationId, HashMap::new, Collectors.toList()))
      .forEach((key, dtoList) -> {
        long batchSize = dtoList.size();
        ImChatReqDTO imChatReqDTO = dtoList.get(0);
        // 对于同一个 chat_relation_id, 如果 senderId 不同, 说明在同一时间(极短的时间内) 两个人都向对方发送了消息
        boolean multiSender = dtoList.stream().anyMatch(dto -> ! Objects.equals(dto.getSenderId(), imChatReqDTO.getSenderId()));
        // 双发几乎在同一时间点向对方发送了消息
        long startSeqNo = 0L;
        if (multiSender) {
          // 发送信息的双发, 依次处理
          String senderCacheKey = messageCacheKeyBuilder.chatRelationMsgCountKey(imChatReqDTO.getSenderId());
          redisTemplate.opsForHash().increment(senderCacheKey, imChatReqDTO.getRelationId(), batchSize);
          String receiverCacheKey = messageCacheKeyBuilder.chatRelationMsgCountKey(imChatReqDTO.getReceiverId());
          Long endSeqNo = redisTemplate.opsForHash().increment(receiverCacheKey, imChatReqDTO.getRelationId(), batchSize);
          startSeqNo = endSeqNo - batchSize + 1; // 19 - 4 + 1 = 16
        } else {
          String senderCacheKey = messageCacheKeyBuilder.chatRelationMsgCountKey(imChatReqDTO.getSenderId());
          Long endSeqNo = redisTemplate.opsForHash().increment(senderCacheKey, imChatReqDTO.getRelationId(), batchSize);
          startSeqNo = endSeqNo - batchSize + 1; // 19 - 4 + 1 = 16
        }
        for (ImChatReqDTO dto : dtoList) {
          dto.setSeqNo(startSeqNo ++);
        }
      });


    /**
     * todo: 后续可以优化, 统计senderId, 减少redis层的io访问
     * 如果cache没有数据, 只能从es层查询, 但是这部分的量会较少一些
     */
    // imChatReqDTOList.parallelStream()
    //   .forEach(imChatReqDTO -> {
    //     String cacheKey = messageCacheKeyBuilder.chatRelationMsgCountKey(imChatReqDTO.getSenderId());
    //     long endSeqNo = redisTemplate.opsForHash().increment(cacheKey, imChatReqDTO.getRelationId(), 1L);
    //     imChatReqDTO.setSeqNo(endSeqNo);
    //   });
  }

  /**
   * 批量保存到 ES
   * @param imChatReqDTOList
   */
  private void syncToEs(List<ImChatReqDTO> imChatReqDTOList) {
    /**
     * 1. 转化为map, key=relationId, value=imChatReqDTO
     */
    Map<String, ImChatReqDTO> imChatReqDTOMap = this.getLatestChatByRelationId(imChatReqDTOList);

    /**
     * 2. 并行执行 3 个 IO 操作
     * 3. 等待全部完成
     */
    CompletableFuture.allOf (
      // 批量保存消息记录
      chatMessageAsyncTaskSubmitter.runAsync("chat-record", () -> chatRecordEsService.bulk(imChatReqDTOList)),
      // 批量保存会话关系信息
      chatMessageAsyncTaskSubmitter.runAsync("chat-relation", () -> chatRelationEsService.bulkMap(imChatReqDTOMap)),
      // 批量更新发件人的收件箱位置
      chatMessageAsyncTaskSubmitter.runAsync("chat-box", () -> chatBoxEsService.bulkMap(imChatReqDTOMap))
    ).whenComplete((v, ex) -> {
      if (ex != null) {
        log.error("syncToEs failed", ex);
      }
    }).join();
  }

  /**
   * 异步化审核处理链路
   * @param messages
   * @return
   */
  private List<ImChatReqDTO> doMsgAuditAsyncHandler(List<MessageExt> messages) {
    List<ImChatReqDTO> imChatReqDTOList = new CopyOnWriteArrayList<>();
    // 使用并行提升速率
    messages.parallelStream().forEach(msg -> {
      ImChatReqDTO imChatReqDTO = this.generateImChatReqDTO(msg);
      // 审核这里需根据内容决定走不同类型的判断/视频, 图片, 音频, 文字的审核耗时不太一样
      this.doMsgAuditHandle(imChatReqDTO);
      if (Optional.ofNullable(imChatReqDTO).map(ImChatReqDTO::getMsgStatus).isEmpty()) {
        log.error("msg audit invalid dto: {}", JSON.toJSONString(imChatReqDTO));
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
        // 审核失败 需要返回失败通知给到发送人
        imChatReqDTO.setReceiverId(imChatReqDTO.getSenderId());
        imChatReqDTO.setMsgStatus(ChatMsgStatusEnum.INVALID_BODY.getCode());
        return;
      }
      imChatReqDTO.setMsgStatus(ChatMsgStatusEnum.SUCCESS.getCode());
    } else if (imMessageService.isImageMessage(imChatReqDTO)) {
      boolean sendStatus = this.doImageAudit(imChatReqDTO);
      if (! sendStatus) {
        // 审核失败 需要返回失败通知给到发送人
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
          Collectors.toList(),
          /**
           * 按seqNo降序排序
           * 获取排序后的第一条 (最新记录), 无数据时返回null
           */
          list -> list.stream()
            .max(Comparator.comparing(ImChatReqDTO::getSeqNo))
            .orElse(null)
        )
      ));
  }
}