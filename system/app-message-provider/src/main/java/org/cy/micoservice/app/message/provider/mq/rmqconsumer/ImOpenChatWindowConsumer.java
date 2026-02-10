package org.cy.micoservice.app.message.provider.mq.rmqconsumer;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.cy.micoservice.app.common.constants.CommonFormatConstants;
import org.cy.micoservice.app.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.app.entity.message.model.provider.pojo.es.ChatBoxEs;
import org.cy.micoservice.app.entity.message.model.provider.req.OpenChatReq;
import org.cy.micoservice.app.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.app.message.provider.config.MessageApplicationProperties;
import org.cy.micoservice.app.message.provider.service.ChatBoxEsService;
import org.cy.micoservice.app.message.provider.constant.MessageChatRelationConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/31
 * @Description: 接收用户打开聊天会话窗口时, 发送过来的消息
 */
@Slf4j
@Component
public class ImOpenChatWindowConsumer implements InitializingBean {

  @Autowired
  private RocketMQConsumerProperties rocketMQConsumerProperties;
  @Autowired
  private MessageApplicationProperties messageApplicationProperties;
  @Autowired
  private ChatBoxEsService chatBoxEsService;

  @Override
  public void afterPropertiesSet() throws Exception {
    Thread consumeMqTask = new Thread(() -> {
      try {
        this.initImOpenChatWindowConsumer();
      } catch (Exception e) {
        log.error("consume im open chat windows msg error:", e);
        throw new RuntimeException(e);
      }
    });
    consumeMqTask.setName("im-open-chat-window-consume-task");
    consumeMqTask.start();
  }

  private void initImOpenChatWindowConsumer() throws MQClientException {
    DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
    mqPushConsumer.setVipChannelEnabled(false);
    mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameserver());
    mqPushConsumer.setConsumerGroup(String.format(CommonFormatConstants.COMMENT_FORMAT_UNDERSCORE_SPLIT, rocketMQConsumerProperties.getGroup(), ImOpenChatWindowConsumer.class.getSimpleName()));
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.setConsumeMessageBatchMaxSize(messageApplicationProperties.getImBizMessageConsumerBatchSize());
    mqPushConsumer.subscribe(messageApplicationProperties.getOpenChatTopic(), "");
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
        this.openChatWindowHandler(messages);
        log.info("receive open chat info");
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
      } catch (Exception e) {
        log.error("consumer message has error:", e);
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
      }
    });
    mqPushConsumer.start();
    log.info("ImOpenChatWindowConsumer started!");
  }

  private void openChatWindowHandler(List<MessageExt> messages) {
    List<ChatBoxEs> chatBoxEsList = messages.stream()
      .map(message -> {
        OpenChatReq openChatReq = JSON.parseObject(message.getBody(), OpenChatReq.class);
        return ChatBoxEs.builder()
          .id(String.format(MessageChatRelationConstants.RELATION_ID_FORMAT, openChatReq.getRelationId(), openChatReq.getUserId()))
          .userId(openChatReq.getUserId())
          .relationId(openChatReq.getRelationId())
          .msgOffset(openChatReq.getSeqNo())
          .updateTime(System.nanoTime())
          .deleted(DeleteStatusEnum.ACTIVE.getCode())
          .build();
      })
      .collect(Collectors.toList());
    chatBoxEsService.bulkList(chatBoxEsList);
  }
}
