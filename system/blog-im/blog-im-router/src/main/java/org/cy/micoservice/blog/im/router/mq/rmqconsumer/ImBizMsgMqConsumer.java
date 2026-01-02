package org.cy.micoservice.blog.im.router.mq.rmqconsumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.cy.micoservice.blog.common.constants.CommonFormatConstants;
import org.cy.micoservice.blog.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.blog.framework.rocketmq.starter.producer.RocketMQProducerClient;
import org.cy.micoservice.blog.im.facade.dto.router.ImSingleMessageDTO;
import org.cy.micoservice.blog.im.router.config.ImRouterProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description: 消费connector给过来的业务消息
 */
@Slf4j
@Component
public class ImBizMsgMqConsumer implements InitializingBean {

  @Autowired
  private ImRouterProperties imRouterProperties;
  @Autowired
  private RocketMQConsumerProperties rocketMQConsumerProperties;
  @Autowired
  private RocketMQProducerClient producerClient;

  @Override
  public void afterPropertiesSet() throws Exception {
    Thread consumeMqTask = new Thread(() -> {
      try {
        initBizMsgMqConsumer();
      } catch (Exception e) {
        log.error("consume im msg error", e);
        throw new RuntimeException(e);
      }
    });
    consumeMqTask.setName("im-biz-consume-task");
    consumeMqTask.start();
  }

  /**
   * 初始化业务消息消费者
   * @throws MQClientException
   */
  private void initBizMsgMqConsumer() throws MQClientException {
    DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
    mqPushConsumer.setVipChannelEnabled(false);
    mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameserver());
    mqPushConsumer.setConsumerGroup(String.format(CommonFormatConstants.COMMENT_FORMAT_UNDERSCORE_SPLIT, rocketMQConsumerProperties.getGroup(), ImBizMsgMqConsumer.class.getSimpleName()));
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.setConsumeMessageBatchMaxSize(imRouterProperties.getImBizMessageConsumerBatchSize());
    mqPushConsumer.subscribe(imRouterProperties.getImBizMessageConsumerTopic(), "");
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
        for (MessageExt msg : messages) {
          this.doBizMsgHandler(msg);
        }
      } catch (Exception e) {
        log.error("consumer message has error,", e);
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    mqPushConsumer.setConsumeThreadMin(1);
    mqPushConsumer.setConsumeThreadMax(1);
    mqPushConsumer.start();
    log.info("ImBizMsgMqConsumer started!");
  }

  /**
   * 执行业务消息处理逻辑
   * @param msg
   */
  private void doBizMsgHandler(MessageExt msg) {
    ImSingleMessageDTO imSingleMessageDTO = JSON.parseObject(msg.getBody(), ImSingleMessageDTO.class);
    Message message = new Message();
    message.setBody(JSONObject.toJSONString(imSingleMessageDTO).getBytes(StandardCharsets.UTF_8));
    // 发送消息给到路由层服务
    message.setTopic(imRouterProperties.getImRouteTopic());
    try {
      SendResult sendResult = producerClient.send(message);
      if (! SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
        log.error("send message error, msg: {}", JSON.toJSONString(msg));
      }
    } catch (Exception e) {
      log.error("send route message error", e);
    }
  }
}