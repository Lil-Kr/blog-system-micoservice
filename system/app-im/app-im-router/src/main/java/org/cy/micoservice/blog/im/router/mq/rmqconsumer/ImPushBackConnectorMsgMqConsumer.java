package org.cy.micoservice.blog.im.router.mq.rmqconsumer;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.cy.micoservice.blog.common.constants.CommonFormatConstants;
import org.cy.micoservice.blog.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.blog.im.facade.dto.router.ImSingleMessageDTO;
import org.cy.micoservice.blog.im.facade.dto.router.ImBatchMessageDTO;
import org.cy.micoservice.blog.im.router.config.ImRouterProperties;
import org.cy.micoservice.blog.im.router.service.ImPushService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description: 接收 消息服务端推送过来的消息, 并推送到目标用户对应的 im-connector 层
 */
@Slf4j
@Component
public class ImPushBackConnectorMsgMqConsumer implements InitializingBean {

  @Autowired
  private ImRouterProperties imRouterProperties;
  @Autowired
  private RocketMQConsumerProperties rocketMQConsumerProperties;
  @Autowired
  private ImPushService imPushService;

  @Override
  public void afterPropertiesSet() throws Exception {
    Thread consumeMqTask = new Thread(() -> {
      try {
        this.initPushBackMsgMqConsumer();
      } catch (Exception e) {
        log.error("consume im msg error", e);
        throw new RuntimeException(e);
      }
    });
    consumeMqTask.setName("im-push-back-consume-task");
    consumeMqTask.start();
  }

  /**
   * 初始化业务消息消费者
   * @throws MQClientException
   */
  private void initPushBackMsgMqConsumer() throws MQClientException {
    DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
    mqPushConsumer.setVipChannelEnabled(false);
    mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameserver());
    mqPushConsumer.setConsumerGroup(String.format(CommonFormatConstants.COMMENT_FORMAT_UNDERSCORE_SPLIT, rocketMQConsumerProperties.getGroup(), ImPushBackConnectorMsgMqConsumer.class.getSimpleName()));
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.setConsumeMessageBatchMaxSize(imRouterProperties.getImRouteTopicConsumerBatchSize());
    mqPushConsumer.subscribe(imRouterProperties.getImRoutePushTopic(), "");
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
        this.doPushBackConnectorMsgHandler(messages);
      } catch (Exception e) {
        log.error("consumer route push back message has error: ", e);
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    mqPushConsumer.start();
    log.info("ImPushBackMsgMqConsumer started!");
  }

  /**
   * 推送消息给到connector服务
   * @param messages
   */
  private void doPushBackConnectorMsgHandler(List<MessageExt> messages) {
    for (MessageExt msg : messages) {
      String json = new String(msg.getBody());
      ImBatchMessageDTO imBatchMessageDTO = JSON.parseObject(json, ImBatchMessageDTO.class);
      List<ImSingleMessageDTO> imSingleMessageDTOList = imBatchMessageDTO.getImSingleMessageDTOList();
      if (CollectionUtils.isEmpty(imSingleMessageDTOList)) return;

      imSingleMessageDTOList.parallelStream()
        .forEach(singleMessageDTO -> {
          try {
            log.info("ImPushBackMsgMqConsumer, singleMessageDTO: {}", singleMessageDTO);
            imPushService.sendSingleMsgToObject(singleMessageDTO);
          } catch (Exception e) {
            log.error("send singleMessageDTO error: {}, param is: {}", e, singleMessageDTO);
          }
        });
    }
  }
}
