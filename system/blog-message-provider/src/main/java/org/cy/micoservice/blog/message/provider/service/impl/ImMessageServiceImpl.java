package org.cy.micoservice.blog.message.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.cy.micoservice.blog.audit.facade.dto.AuditMsgDTO;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultMessageDTO;
import org.cy.micoservice.blog.audit.facade.dto.text.ChatTextAuditBody;
import org.cy.micoservice.blog.audit.facade.dto.text.TextAuditBody;
import org.cy.micoservice.blog.audit.facade.enums.AuditTypeEnum;
import org.cy.micoservice.blog.audit.facade.enums.TextAuditBodyTypeEnum;
import org.cy.micoservice.blog.audit.facade.interfaces.TextAuditFacade;
import org.cy.micoservice.blog.common.base.provider.RpcResponse;
import org.cy.micoservice.blog.common.enums.exception.BizErrorEnum;
import org.cy.micoservice.blog.common.utils.AssertUtil;
import org.cy.micoservice.blog.framework.rocketmq.starter.producer.RocketMQProducerClient;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatContentDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReqDTO;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.cy.micoservice.blog.message.provider.service.ImMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: im消息处理service
 */
@Slf4j
@Service
public class ImMessageServiceImpl implements ImMessageService {

  @Autowired
  private RocketMQProducerClient rocketMQProducerClient;
  @Autowired
  private MessageApplicationProperties messageApplicationProperties;
  @DubboReference(check = false)
  private TextAuditFacade textAuditFacade;

  /**
   * send msg to mq for audit service
   * @param imChatReqDTO
   * @return
   */
  @Override
  public boolean sendAuditMessageToMQ(ImChatReqDTO imChatReqDTO) {
    String content = imChatReqDTO.getContent();
    ImChatContentDTO imChatContentDTO = JSON.parseObject(content, ImChatContentDTO.class);
    Integer type = imChatContentDTO.getType();

    // 图片/文字 均可走mq审核
    if (! AuditTypeEnum.TEXT.getCode().equals(type) && ! AuditTypeEnum.IMAGE.getCode().equals(type)) return false;

    // 文本数据信息, 投递到audit服务去
    AuditMsgDTO auditMsgDTO = this.generateAuditMsgDTO(imChatContentDTO, imChatReqDTO.getMsgId());
    log.info("send to audit service: auditMsgDTO: {}", auditMsgDTO);
    return this.sendImChatAuditMq(auditMsgDTO);
  }

  @Override
  public AuditResultMessageDTO getTextAuditMessageResult(ImChatReqDTO imChatReqDTO) {
    AssertUtil.isTrue(this.isTextMessage(imChatReqDTO), BizErrorEnum.PARAM_ERROR);
    ImChatContentDTO chatReqContent = JSON.parseObject(imChatReqDTO.getContent(), ImChatContentDTO.class);
    AuditMsgDTO auditMsgDTO = this.generateAuditMsgDTO(chatReqContent, imChatReqDTO.getMsgId());
    RpcResponse<AuditResultMessageDTO> rpcResponse = textAuditFacade.checkTextValid(auditMsgDTO);
    AssertUtil.isTrue(rpcResponse.isSuccess(), BizErrorEnum.SYSTEM_ERROR);
    return rpcResponse.getData();
  }

  @Override
  public boolean isTextMessage(ImChatReqDTO imChatReqDTO) {
    String content = imChatReqDTO.getContent();
    ImChatContentDTO chatReqContentDTO = JSON.parseObject(content, ImChatContentDTO.class);
    return AuditTypeEnum.TEXT.getCode().equals(chatReqContentDTO.getType());
  }

  @Override
  public boolean isImageMessage(ImChatReqDTO imChatReqDTO) {
    String content = imChatReqDTO.getContent();
    ImChatContentDTO chatReqContent = JSON.parseObject(content, ImChatContentDTO.class);
    return AuditTypeEnum.IMAGE.getCode().equals(chatReqContent.getType());
  }

  /**
   * 生成文本审核消息 DTO
   * @param imChatContentDTO
   * @param msgId
   * @return
   */
  private AuditMsgDTO generateAuditMsgDTO(ImChatContentDTO imChatContentDTO, String msgId) {
    ChatTextAuditBody chatTextAuditBody = new ChatTextAuditBody();
    chatTextAuditBody.setMsgId(msgId);
    chatTextAuditBody.setContent(imChatContentDTO.getBody());

    TextAuditBody textAuditBody = new TextAuditBody();
    textAuditBody.setBodyType(TextAuditBodyTypeEnum.CHAT.getCode());
    textAuditBody.setBody(JSON.toJSONString(chatTextAuditBody));

    AuditMsgDTO auditMsgDTO = new AuditMsgDTO();
    auditMsgDTO.setAuditType(AuditTypeEnum.TEXT.getCode());
    auditMsgDTO.setAuditBody(JSON.toJSONString(textAuditBody));
    auditMsgDTO.setEventTime(System.nanoTime());
    return auditMsgDTO;
  }

  /**
   * 发送文本审核mq
   * @param auditMsgDTO
   */
  private boolean sendImChatAuditMq(AuditMsgDTO auditMsgDTO) {
    Message message = new Message();
    message.setTopic(messageApplicationProperties.getImChatMessageAuditTopic());
    message.setBody(JSONObject.toJSONString(auditMsgDTO).getBytes(StandardCharsets.UTF_8));
    try {
      SendResult sendResult = rocketMQProducerClient.send(message);
      if (!SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
        log.error("send audit result msg status:{}, body: {}", sendResult.getSendStatus(), auditMsgDTO);
        return false;
      }
      log.info("send audit result msg success, body: {}", auditMsgDTO);
      return true;
    } catch (Exception e) {
      log.error("send sync msg error: {}, body: {}", e, auditMsgDTO);
    }
    return false;
  }
}
