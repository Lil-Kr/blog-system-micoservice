package org.cy.micoservice.blog.message.provider.mq.rmqconsumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultMessageDTO;
import org.cy.micoservice.blog.audit.facade.enums.AuditResultCodeEnum;
import org.cy.micoservice.blog.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.blog.framework.rocketmq.starter.producer.RocketMQProducerClient;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/16
 * @Description: 消费者: im消息审核结果
 */
@Slf4j
@Component
public class ImChatAuditMessageConsumer implements InitializingBean {

  @Autowired
  private RocketMQConsumerProperties rocketMQConsumerProperties;
  @Autowired
  private RocketMQProducerClient rocketMQProducerClient;
  @Autowired
  private MessageApplicationProperties messageApplicationProperties;

  @Override
  public void afterPropertiesSet() throws Exception {
    Thread consumeMqTask = new Thread(() -> {
      try {
        this.initChatMessageAuditConsumer();
      } catch (Exception e) {
        log.error("consume chat msg error", e);
        throw new RuntimeException(e);
      }
    });
    consumeMqTask.setName("chat-msg-audit-consume-task");
    consumeMqTask.start();
  }

  /**
   * 初始化会话消息消费者
   */
  private void initChatMessageAuditConsumer() throws MQClientException {
    DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
    mqPushConsumer.setVipChannelEnabled(false);
    mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameserver());
    mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroup() + "_" + ImChatAuditMessageConsumer.class.getSimpleName());
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.setConsumeMessageBatchMaxSize(messageApplicationProperties.getImBizMessageConsumerBatchSize());
    mqPushConsumer.subscribe(messageApplicationProperties.getImChatMessageAuditResultTopic(), "");
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
        for (MessageExt msg : messages) {
          this.doChatAuditResultMessageHandle(msg);
        }
      } catch (Exception e) {
        log.error("consumer message has error,", e);
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    mqPushConsumer.start();
    log.info("ImChatAuditMessageConsumer started!");
  }

  /**
   * 收到审核的im消息之后, 做的业务处理逻辑
   * @param msg
   */
  private void doChatAuditResultMessageHandle(MessageExt msg) {
    // 针对之前的审核结果, 查询消息内容, 然后根据结果状态判断是否需要推送内容
    AuditResultMessageDTO auditResult = JSON.parseObject(msg.getBody(), AuditResultMessageDTO.class);
    log.info("receive audit msg: {}", JSONObject.toJSONString(auditResult));
    if (! AuditResultCodeEnum.VALID.getCode().equals(auditResult.getCode())) {
      return;
    }

    /**
     * 审核成功
     * todo: 1. 更改DB中的消息状态, 改为审核通过
     * todo: 2. 发送消息给router层做后续逻辑处理
     */
    return;
  }

}