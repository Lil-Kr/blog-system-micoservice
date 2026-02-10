package org.cy.micoservice.blog.im.router.mq.rmqconsumer;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.cy.micoservice.blog.common.constants.CommonFormatConstants;
import org.cy.micoservice.blog.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.blog.im.facade.dto.connector.ImMessageDTO;
import org.cy.micoservice.blog.im.facade.dto.connector.body.ImLoginBody;
import org.cy.micoservice.blog.im.router.config.ImRouterProperties;
import org.cy.micoservice.blog.im.router.service.UserConnectorMappingService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/16
 * @Description: 消费者: 监听 IM 用户登录信号
 */
@Slf4j
@Component
public class ImLoginMsgMqConsumer implements InitializingBean {

  @Autowired
  private ImRouterProperties imRouterProperties;
  @Autowired
  private RocketMQConsumerProperties rocketMQConsumerProperties;
  @Autowired
  private UserConnectorMappingService userConnectorMappingService;

  @Override
  public void afterPropertiesSet() {
    Thread consumeMqTask = new Thread(() -> {
      try {
        this.initLoginMqMessageConsumer();
      } catch (Exception e) {
        log.error("consume im msg error", e);
        throw new RuntimeException(e);
      }
    });
    consumeMqTask.setName("im-login-router-task");
    consumeMqTask.start();
  }

  /**
   * 初始化登录消息消费者
   * @throws MQClientException
   */
  public void initLoginMqMessageConsumer() throws MQClientException {
    DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
    mqPushConsumer.setVipChannelEnabled(false);
    mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameserver());
    mqPushConsumer.setConsumerGroup(String.format(CommonFormatConstants.COMMENT_FORMAT_UNDERSCORE_SPLIT, rocketMQConsumerProperties.getGroup(), ImLoginMsgMqConsumer.class.getSimpleName()));
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.setConsumeMessageBatchMaxSize(imRouterProperties.getImLoginConsumerBatchSize());
    mqPushConsumer.subscribe(imRouterProperties.getImLoginConsumerTopic(), "");
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
          /**
           * 审核需根据内容决定走不同类型的判断: 视频, 图片, 音频, 文字的审核耗时不太一样
           */
          this.doImLoginMqHandler(messages);
      } catch (Exception e) {
        log.error("consumer message has error,", e);
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    mqPushConsumer.start();
    log.info("ImLoginMsgMqConsumer started!");
  }

  /**
   * 处理登录消息, 写入 redis
   * @param messages
   */
  private void doImLoginMqHandler(List<MessageExt> messages) {
    for (MessageExt msg : messages) {
      try {
        String bodyJson = new String(msg.getBody());
        ImMessageDTO dto = JSON.parseObject(bodyJson, ImMessageDTO.class);
        log.info("receive login info: {}", dto);
        String imLoginMsgBodyJson = dto.getBody();
        ImLoginBody imLoginBody = JSON.parseObject(imLoginMsgBodyJson, ImLoginBody.class);
        userConnectorMappingService.saveAddressByUserId(imLoginBody.getUserId(), imLoginBody.getImConnectorAddress());
        log.info("login mq handler: {}", bodyJson);
      }catch (Exception e) {
        log.error("do im login mq handler error", e);
      }
    }
  }
}
