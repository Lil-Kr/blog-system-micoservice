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
import org.cy.micoservice.blog.im.facade.dto.connector.body.ImLogoutBody;
import org.cy.micoservice.blog.im.router.config.ImRouterProperties;
import org.cy.micoservice.blog.im.router.config.ImRouterRedisKeyBuilder;
import org.cy.micoservice.blog.im.router.service.UserConnectorMappingService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description:
 */
@Slf4j
@Component
public class ImLogoutMsgMqConsumer implements InitializingBean {

  @Autowired
  private ImRouterProperties imRouterProperties;
  @Autowired
  private RocketMQConsumerProperties rocketMQConsumerProperties;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Autowired
  private ImRouterRedisKeyBuilder imRouterRedisKeyBuilder;
  @Autowired
  private UserConnectorMappingService userConnectorMappingService;

  @Override
  public void afterPropertiesSet() {
    Thread consumeMqTask = new Thread(() -> {
      try {
        this.initLogoutMqConsumer();
      } catch (Exception e) {
        log.error("consume im msg error", e);
        throw new RuntimeException(e);
      }
    });
    consumeMqTask.setName("im-logout-consume-task");
    consumeMqTask.start();
  }

  private void initLogoutMqConsumer() throws MQClientException {
    DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
    mqPushConsumer.setVipChannelEnabled(false);
    mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameserver());
    mqPushConsumer.setConsumerGroup(String.format(CommonFormatConstants.COMMENT_FORMAT_UNDERSCORE_SPLIT, rocketMQConsumerProperties.getGroup(), ImLogoutMsgMqConsumer.class.getSimpleName()));
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.setConsumeMessageBatchMaxSize(imRouterProperties.getImLogoutConsumerBatchSize());
    mqPushConsumer.subscribe(imRouterProperties.getImLogoutConsumerTopic(), "");
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
        for (MessageExt msg : messages) {
          this.doImLogoutMqHandler(msg);
        }
      } catch (Exception e) {
        log.error("consumer message has error,", e);
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    mqPushConsumer.start();
    log.info("ImLogoutMsgMqConsumer started!");
  }

  /**
   * 处理im登出消息
   * @param ext
   */
  private void doImLogoutMqHandler(MessageExt ext) {
    try {
      String bodyJson = new String(ext.getBody());
      ImMessageDTO logoutImMessageDTO = JSON.parseObject(bodyJson, ImMessageDTO.class);
      String imLogoutMsgBodyJson = logoutImMessageDTO.getBody();
      ImLogoutBody logoutBody = JSON.parseObject(imLogoutMsgBodyJson, ImLogoutBody.class);
      userConnectorMappingService.remove(logoutBody.getUserId());
      log.info("logout mq handler: {}", bodyJson);
    }catch (Exception e) {
      log.error("doImLogoutMqHandler error",e);
    }
  }
}