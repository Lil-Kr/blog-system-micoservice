package org.cy.micoservice.blog.audit.provider.mq.rmqconsumer;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
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
import org.cy.micoservice.blog.audit.facade.dto.NoteTextAuditBody;
import org.cy.micoservice.blog.audit.facade.dto.text.TextAuditBody;
import org.cy.micoservice.blog.audit.facade.enums.AuditTypeEnum;
import org.cy.micoservice.blog.audit.facade.enums.TextAuditBodyTypeEnum;
import org.cy.micoservice.blog.audit.provider.config.AuditApplicationProperties;
import org.cy.micoservice.blog.audit.provider.handler.AuditManager;
import org.cy.micoservice.blog.audit.provider.service.AuditLogService;
import org.cy.micoservice.blog.common.constants.CommonFormatConstants;
import org.cy.micoservice.blog.entity.audit.model.facade.po.AuditLog;
import org.cy.micoservice.blog.audit.facade.enums.AuditRefTypeEnum;
import org.cy.micoservice.blog.framework.rocketmq.starter.consumer.RocketMQConsumerProperties;
import org.cy.micoservice.blog.framework.rocketmq.starter.producer.RocketMQProducerClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/16
 * @Description: 统一审核内容主题-消费者
 */
@Slf4j
@Component
public class CommonAuditConsumer {

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
  public void initCommonAuditConsumer() throws MQClientException {
    DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
    mqPushConsumer.setVipChannelEnabled(false);
    mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameserver());
    mqPushConsumer.setConsumerGroup(String.format(CommonFormatConstants.COMMENT_FORMAT_UNDERSCORE_SPLIT, rocketMQConsumerProperties.getGroup(), CommonAuditConsumer.class.getSimpleName()));
    mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    mqPushConsumer.setConsumeMessageBatchMaxSize(200);
    mqPushConsumer.subscribe(auditApplicationProperties.getCommonAuditTopic(), "");
    mqPushConsumer.setMessageListener(new MessageListenerConcurrently() {

      @Override
      public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
        try {
          List<AuditLog> auditLogList = new ArrayList<>();
          for (MessageExt msg : messages) {
            AuditMsgDTO auditMsgDTO = JSON.parseObject(msg.getBody(), AuditMsgDTO.class);
            log.info("receive message: {}", auditMsgDTO);
            if (AuditTypeEnum.TEXT.getCode().equals(auditMsgDTO.getAuditType())) {
              // 优先使用接口响应速率低的审核平台
              AuditResultDTO auditResultDTO = auditManager.doTextAudit(auditMsgDTO);
              AuditLog auditLog = handleAuditResult(auditResultDTO, auditMsgDTO, auditResultDTO.getChannelName());
              if (auditLog != null) {
                auditLogList.add(auditLog);
              }
            }
          }
          auditLogService.saveBatch(auditLogList);
        } catch (Exception e) {
          log.error("consumer common audit message has error,",e);
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
      }
    });
    mqPushConsumer.start();
    log.info("CommonAuditConsumer is started!");
  }

  /**
   * 处理审核结果内容
   * @param auditResultDTO
   * @param auditMsgDTO
   * @param channelName
   * @return
   */
  private AuditLog handleAuditResult(AuditResultDTO auditResultDTO, AuditMsgDTO auditMsgDTO, String channelName) {
    //审记日志的记录
    TextAuditBody textAuditBody = JSON.parseObject(auditMsgDTO.getAuditBody(), TextAuditBody.class);
    if (TextAuditBodyTypeEnum.NOTE.getCode().equals(textAuditBody.getBodyType())) {
      NoteTextAuditBody noteTextAuditBody = JSON.parseObject(textAuditBody.getBody(), NoteTextAuditBody.class);

      AuditResultMessageDTO auditResultMessageDTO = new AuditResultMessageDTO();
      auditResultMessageDTO.setRefId(noteTextAuditBody.getNoteId());
      auditResultMessageDTO.setRefType(AuditRefTypeEnum.NOTE_TEXT.getCode());
      BeanUtils.copyProperties(auditResultDTO, auditResultMessageDTO);
      //发送mq
      sendResponseMsg(auditResultMessageDTO);

      AuditLog auditLog = new AuditLog();
      auditLog.setResultCode(auditResultMessageDTO.getCode());
      auditLog.setMessage(auditResultMessageDTO.getMessage());
      auditLog.setRefId(String.valueOf(noteTextAuditBody.getNoteId()));
      auditLog.setEventTime(auditMsgDTO.getEventTime());
      auditLog.setRefType(AuditRefTypeEnum.NOTE_TEXT.getCode());
      auditLog.setChannel(channelName);

      return auditLog;
    }
    return null;
  }

  /**
   *
   * @param auditResultMessageDTO
   */
  private void sendResponseMsg(AuditResultMessageDTO auditResultMessageDTO) {
    Message message = new Message();
    message.setTopic(auditApplicationProperties.getCommonAuditResultTopic());
    message.setBody(JSON.toJSONBytes(auditResultMessageDTO));
    try {
      SendResult sendResult = rocketMQProducerClient.send(message);
      if (!SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
        log.error("send audit result msg status:{},body:{}", sendResult.getSendStatus(), auditResultMessageDTO);
      } else {
        log.info("send audit result msg success, body:{}", auditResultMessageDTO);
      }
    } catch (Exception e) {
      log.error("send sync msg error:{}, body:{}", e, auditResultMessageDTO);
    }

  }
}