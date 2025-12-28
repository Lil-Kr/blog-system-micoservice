package org.cy.micoservice.blog.audit.provider.mq.rmqconsumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.cy.micoservice.blog.audit.facade.dto.AuditMsgDTO;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultDTO;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultMessageDTO;
import org.cy.micoservice.blog.audit.facade.dto.text.ChatTextAuditBody;
import org.cy.micoservice.blog.audit.facade.dto.text.TextAuditBody;
import org.cy.micoservice.blog.audit.facade.enums.AuditResultCodeEnum;
import org.cy.micoservice.blog.audit.facade.enums.AuditTypeEnum;
import org.cy.micoservice.blog.audit.facade.enums.TextAuditBodyTypeEnum;
import org.cy.micoservice.blog.audit.provider.config.AuditApplicationProperties;
import org.cy.micoservice.blog.audit.provider.handler.AuditManager;
import org.cy.micoservice.blog.audit.provider.service.AuditLogService;
import org.cy.micoservice.blog.entity.audit.model.facade.po.AuditLog;
import org.cy.micoservice.blog.audit.facade.enums.AuditRefTypeEnum;
import org.cy.micoservice.blog.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.blog.framework.rocketmq.starter.producer.RocketMQProducerClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 审核 聊天消息 消费者
 */
@Slf4j
@Component
public class ImBizChatAuditConsumer {

  @Autowired
  private RocketMQConsumerProperties rocketMQConsumerProperties;
  @Autowired
  private RocketMQProducerClient rocketMQProducerClient;
  @Autowired
  private AuditApplicationProperties auditApplicationProperties;
  @Autowired
  private AuditManager auditManager;
  @Autowired
  private AuditLogService auditLogService;

  @PostConstruct
  public void initImChatAuditConsumer() throws MQClientException {
    DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
    mqPushConsumer.setVipChannelEnabled(false);
    mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameserver());
    mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroup() + "_" + ImBizChatAuditConsumer.class.getSimpleName());
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.setConsumeMessageBatchMaxSize(auditApplicationProperties.getImChatMessageAuditTopicPushSize());
    mqPushConsumer.subscribe(auditApplicationProperties.getImChatMessageAuditTopic(), "");
    mqPushConsumer.setMessageListener((MessageListenerConcurrently) (messages, context) -> {
      try {
        List<AuditLog> auditLogList = new ArrayList<>();
        for (MessageExt msg : messages) {
          AuditMsgDTO auditMsgDTO = JSON.parseObject(msg.getBody(), AuditMsgDTO.class);
          log.info("receive message: {}", auditMsgDTO);
          AuditLog auditLog = null;

          if (AuditTypeEnum.TEXT.getCode().equals(auditMsgDTO.getAuditType())) {
            // 优先使用接口响应速率低的审核平台
            AuditResultDTO auditResultDTO = auditManager.doTextAudit(auditMsgDTO);
            auditLog = this.handleAuditResult(auditResultDTO, auditMsgDTO, auditResultDTO.getChannelName());
          } else if (AuditTypeEnum.IMAGE.getCode().equals(auditMsgDTO.getAuditType())) {
            AuditResultDTO auditResultDTO = auditManager.doImagesAudit(auditMsgDTO);
            auditLog = this.handleAuditResult(auditResultDTO, auditMsgDTO, auditResultDTO.getChannelName());
          }

          if (auditLog != null) {
            auditLogList.add(auditLog);
          }
        }

        /**
         * 存储成本比较高
         * 只需要存储不合法的内容即可
         */
        if (CollectionUtils.isNotEmpty(auditLogList)) {
          auditLogService.saveBatch(auditLogList);
        }
      } catch (Exception e) {
        log.error("consumer chat audit message has error:", e);
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    mqPushConsumer.start();
    log.info("ImChatAuditConsumer is started!");
  }

  /**
   * 处理审核结果内容
   * @param auditResultDTO
   * @param auditMsgDTO
   * @param channelName
   * @return
   */
  private AuditLog handleAuditResult(AuditResultDTO auditResultDTO, AuditMsgDTO auditMsgDTO, String channelName) {
    // 审记日志的记录
    TextAuditBody textAuditBody = JSON.parseObject(auditMsgDTO.getAuditBody(), TextAuditBody.class);
    if (! TextAuditBodyTypeEnum.CHAT.getCode().equals(textAuditBody.getBodyType())) {
      return null;
    }

    ChatTextAuditBody chatTextAuditBody = JSON.parseObject(textAuditBody.getBody(), ChatTextAuditBody.class);
    AuditResultMessageDTO auditResultMessageDTO = new AuditResultMessageDTO();
    BeanUtils.copyProperties(auditResultDTO, auditResultMessageDTO);
    auditResultMessageDTO.setRefType(AuditRefTypeEnum.CHAT_TEXT.getCode());
    auditResultMessageDTO.setRefId(chatTextAuditBody.getMsgId());

    /**
     * 发送 mq, 回写到消息服务
     */
    this.sendResponseMsg(auditResultMessageDTO);
    if (! AuditResultCodeEnum.VALID.getCode().equals(auditResultDTO.getCode())) {
      AuditLog auditLog = new AuditLog();
      auditLog.setResultCode(auditResultDTO.getCode());
      auditLog.setMessage(auditResultDTO.getMessage());
      auditLog.setRefId(chatTextAuditBody.getMsgId());
      auditLog.setEventTime(auditMsgDTO.getEventTime());
      auditLog.setRefType(AuditRefTypeEnum.CHAT_TEXT.getCode());
      auditLog.setChannel(channelName);
      return auditLog;
    }

    return null;
  }

  /**
   * 发送审核结果内容到 mq 中
   * @param auditResultMessageDTO
   */
  private void sendResponseMsg(AuditResultMessageDTO auditResultMessageDTO) {
    Message message = new Message();
    message.setTopic(auditApplicationProperties.getImChatMessageAuditResultTopic());
    message.setBody(JSONObject.toJSONString(auditResultMessageDTO).getBytes(StandardCharsets.UTF_8));
    try {
      SendResult sendResult = rocketMQProducerClient.send(message);
      if (! SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
        log.error("send audit result msg error, status: {}, body: {}", sendResult.getSendStatus(), auditResultMessageDTO);
      }
      log.info("send audit result msg success, body: {}", auditResultMessageDTO);
    } catch (Exception e) {
      log.error("send sync msg error, body: {}, error: {}", auditResultMessageDTO, e.getMessage());
    }
  }
}