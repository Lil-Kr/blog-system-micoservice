package org.cy.micoservice.blog.message.provider.mq.rmqconsumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.cy.micoservice.blog.common.constants.CommonFormatConstants;
import org.cy.micoservice.blog.entity.message.model.provider.po.es.ChatBoxEs;
import org.cy.micoservice.blog.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRecordRespDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.cy.micoservice.blog.message.provider.config.MessageCacheKeyBuilder;
import org.cy.micoservice.blog.message.provider.config.async.ChatMessageAsyncTaskSubmitter;
import org.cy.micoservice.blog.message.provider.service.ChatBoxEsService;
import org.cy.micoservice.blog.message.provider.service.ChatRecordEsService;
import org.cy.micoservice.blog.message.provider.service.ChatRelationEsService;
import org.cy.micoservice.blog.user.facade.provider.req.UserEnterInitReqDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/31
 * @Description: 未读消息的提前预加载consumer
 */
@Slf4j
@Component
public class UnReadMessagePullConsumer implements InitializingBean {

  @Resource
  private RocketMQConsumerProperties rocketMQConsumerProperties;
  @Resource
  private MessageApplicationProperties applicationProperties;
  @Resource
  private ChatBoxEsService chatBoxEsService;
  @Resource
  private ChatRelationEsService chatRelationEsService;
  @Resource
  private ChatRecordEsService chatRecordEsService;
  @Resource
  private RedisTemplate<String, String> redisTemplate;
  @Resource
  private MessageCacheKeyBuilder cacheKeyBuilder;
  @Autowired
  private ChatMessageAsyncTaskSubmitter chatMessageAsyncTaskSubmitter;

  @Override
  public void afterPropertiesSet() throws Exception {
    Thread consumeMqTask = new Thread(() -> {
      try {
        this.initUnReadMessagePullConsumer();
      } catch (Exception e) {
        log.error("consume un-read msg pull error", e);
        throw new RuntimeException(e);
      }
    });
    consumeMqTask.setName("un-read-msg-pull-consume-task");
    consumeMqTask.start();
    log.info("UnReadMessagePullConsumer started!");
  }

  private void initUnReadMessagePullConsumer() throws MQClientException {
    DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
    mqPushConsumer.setVipChannelEnabled(false);
    mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameserver());
    mqPushConsumer.setConsumerGroup(String.format(CommonFormatConstants.COMMENT_FORMAT_UNDERSCORE_SPLIT, rocketMQConsumerProperties.getGroup(), UnReadMessagePullConsumer.class.getSimpleName()));
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.setConsumeMessageBatchMaxSize(applicationProperties.getImBizMessageConsumerBatchSize());
    mqPushConsumer.subscribe(applicationProperties.getUserEnterTopic(), "");
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
        this.unReadMsgCheckAndPullHandler(messages);
      } catch (Exception e) {
        log.error("consumer message has error,", e);
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    mqPushConsumer.start();
    log.info("ImBizMessageConsumer started!");
  }

  /**
   * 检查用户是否有未读消息
   * @param messages
   */
  private void unReadMsgCheckAndPullHandler(List<MessageExt> messages) {
    for (MessageExt msg : messages) {
      UserEnterInitReqDTO  userEnterInitReqDTO = JSON.parseObject(msg.getBody(), UserEnterInitReqDTO.class);
      Long userId = userEnterInitReqDTO.getUserId();

      // 查询当前用户下, 每个会话关系的已读 offset
      CompletableFuture<List<ChatRelationRespDTO>> relationFuture =
        chatMessageAsyncTaskSubmitter.supplyAsync(
          "query-chat-relation",
          () -> chatRelationEsService.listByUserIdOrReceiverId(userId)
        );

      // 查询当前用户收信箱的已读offset
      CompletableFuture<List<ChatBoxEs>> chatBoxFuture =
        chatMessageAsyncTaskSubmitter.supplyAsync(
          "query-chat-box",
          () -> chatBoxEsService.listByUserId(userId)
        );
      CompletableFuture.allOf(relationFuture, chatBoxFuture).join();

      // 获取返回的 chat_relation 数据
      List<ChatRelationRespDTO> chatRelationRespDTOList = relationFuture.join();
      // 获取返回的 chat_box 数据
      List<ChatBoxEs> chatBoxEsList = chatBoxFuture.join();
      Map<String, ChatBoxEs> chatBoxEsMap = chatBoxEsList.stream()
        .collect(Collectors.toMap(ChatBoxEs::getRelationId, item -> item));

      for (ChatRelationRespDTO chatRelationRespDTO : chatRelationRespDTOList) {
        ChatBoxEs chatBoxEs = chatBoxEsMap.get(chatRelationRespDTO.getRelationId());
        // 当前会话的总记录数
        long msgCount = chatRelationRespDTO.getMsgCount();

        // 当前用户的收信箱已读位置
        long curReadMsgOffset = chatBoxEs.getMsgOffset();
        /**
         * 对比 chatBox中的已读offset 与 chat_relation中的 msgCount总数, 如果一致说明没有未读消息
         */
        if (msgCount <= curReadMsgOffset) continue;

        /**
         * 用户的未读消息内容 这个操作会比较慢, 目前只加载未读消息的前100条
         */
        List<ChatRecordRespDTO> unReadMsgList = chatRecordEsService.queryFromOffset(curReadMsgOffset + 1, msgCount, chatBoxEs.getRelationId());
        Map<String, String> chatRecordRespDTOMap = new HashMap<>();
        Set<ZSetOperations.TypedTuple<String>> chatSeqSet = new HashSet<>();
        for (ChatRecordRespDTO chatRecordRespDTO : unReadMsgList) {
          chatRecordRespDTOMap.put(String.valueOf(chatRecordRespDTO.getChatId()), JSONObject.toJSONString(chatRecordRespDTO));
          // 对应会话的消息排序
          ZSetOperations.TypedTuple<String> tuple = new DefaultTypedTuple<>(String.valueOf(chatRecordRespDTO.getChatId()), (double) chatRecordRespDTO.getSeqNo());
          chatSeqSet.add(tuple);
        }

        /**
         * todo: 这里量非常大, 实际生产上, 必须是 Redis Cluster 集群架构
         * 缓存: 当前用户未读的消息记录
         */
        String cacheKey = cacheKeyBuilder.buildChatSeqZSetKey(userId, chatBoxEs.getRelationId());
        redisTemplate.opsForZSet().add(cacheKey, chatSeqSet);
        redisTemplate.expire(cacheKey,1, TimeUnit.DAYS);

        String chatRecordKey = cacheKeyBuilder.buildChatRecordKey(userId, chatBoxEs.getRelationId());
        redisTemplate.opsForHash().putAll(chatRecordKey, chatRecordRespDTOMap);
        redisTemplate.expire(chatRecordKey, 1, TimeUnit.DAYS);
      }
    }
  }
}
