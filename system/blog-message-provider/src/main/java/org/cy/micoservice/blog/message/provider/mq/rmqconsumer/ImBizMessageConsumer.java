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
import org.cy.micoservice.blog.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.blog.im.facade.router.connector.dto.ImMessageDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReq;
import org.cy.micoservice.blog.message.facade.enums.ChatMsgStatusEnum;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.cy.micoservice.blog.message.provider.service.ImMessageService;
import org.cy.micoservice.blog.message.provider.service.ImPushService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
  private ImPushService imPushService;

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
    mqPushConsumer.setConsumeMessageBatchMaxSize(messageApplicationProperties.getImBizMessageConsumerBatchSize());
    mqPushConsumer.subscribe(messageApplicationProperties.getImBizMessageConsumerTopic(), "");
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
        for (MessageExt msg : messages) {
          /**
           * todo: 1.消息存储
           * 审核需根据内容决定走不同类型的判断: 视频, 图片, 音频, 文字的审核耗时不太一样
           */
          this.doMsgAuditAndPushToRouter(msg);
        }
      } catch (Exception e) {
        log.error("consumer message has error:", e);
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    mqPushConsumer.start();
    log.info("ImBizMessageConsumer started!");
  }

  /**
   * 收到im消息之后, 发送到审核平台
   * @param msg
   */
  private void doMsgAuditAndPushToRouter(MessageExt msg) {
    ImChatReq imChatReq = this.generateImChatReqDTO(msg);
    if (imChatReq == null) {
      return;
    }
    log.info("receive message provider push back, imChatReq: {}", imChatReq);

    // 文本审核
    if (imMessageService.isTextMessage(imChatReq)) {
      boolean auditStatus = this.doTextAudit(imChatReq);
      /**
       * 审核失败 需要返回失败通知给到发送人
       */
      if (! auditStatus) {
        imChatReq.setRelationId(imChatReq.getSenderId());
        imChatReq.setMsgStatus(ChatMsgStatusEnum.INVALID_BODY.getCode());
      }
      log.info("text auditStatus: {}", auditStatus);
      // push to im-router
      imPushService.pushRouterSingleMessage(imChatReq);
    }

    // 图片审核
    if (imMessageService.isImageMessage(imChatReq)) {
      boolean sendStatus = this.doImageAudit(imChatReq);
      // 审核失败 需要返回失败通知给到发送人
      if (! sendStatus) {
        imChatReq.setRelationId(imChatReq.getSenderId());
        imChatReq.setMsgStatus(ChatMsgStatusEnum.SYSTEM_ERROR.getCode());
      }
      log.info("image sendStatus: {}", sendStatus);
      // push to im-router
      imPushService.pushRouterSingleMessage(imChatReq);
    }
  }

  /**
   * 执行文本审核, 走rpc调用
   * @param imChatReq
   * @return
   */
  private boolean doTextAudit(ImChatReq imChatReq) {
    try {
      // 文本消息可以尝试走rpc调用
      AuditResultMessageDTO auditResult = imMessageService.getTextAuditMessageResult(imChatReq);
      return AuditResultCodeEnum.VALID.getCode().equals(auditResult.getCode());
    } catch (BizException e) {
      log.error("text audit error: ", e);
    }
    return false;
  }

  /**
   * 执行图片审核, 发送到 mq
   * @param imChatReq
   * @return
   */
  private boolean doImageAudit(ImChatReq imChatReq) {
    boolean sendStatus = imMessageService.sendAuditMessageToMQ(imChatReq);
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
  private ImChatReq generateImChatReqDTO(MessageExt msg) {
    String imMessageBodyJson = new String(msg.getBody());
    try {
      ImMessageDTO imMessageDTO = JSON.parseObject(imMessageBodyJson, ImMessageDTO.class);
      String imBody = imMessageDTO.getBody();

      // 提前解析对象格式, 同时注入消息id
      ImChatReq imChatReq = JSON.parseObject(imBody, ImChatReq.class);
      imChatReq.setMsgId(UUID.randomUUID().toString());
      imChatReq.setTraceId(imMessageDTO.getTraceId());
      imChatReq.setSenderId(imMessageDTO.getSenderId());
      return imChatReq;
    } catch (Exception e) {
      log.error("invalid msg body, msg is: {}", imMessageBodyJson);
    }
    return null;
  }
}