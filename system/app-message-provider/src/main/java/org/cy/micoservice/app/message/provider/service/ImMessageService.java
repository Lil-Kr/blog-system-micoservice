package org.cy.micoservice.app.message.provider.service;

import org.cy.micoservice.app.audit.facade.dto.AuditResultMessageDTO;
import org.cy.micoservice.app.message.facade.dto.req.im.ImChatReqDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: im消息处理
 */
public interface ImMessageService {

  /**
   * 发送审核mq消息 (兜底逻辑)
   * @param imChatReqDTO
   * @return
   */
  boolean sendAuditMessageToMQ(ImChatReqDTO imChatReqDTO);

  /**
   * rpc获取文本审核结果 (更快速)
   * @param imChatReqDTO
   * @return
   */
  AuditResultMessageDTO getTextAuditMessageResult(ImChatReqDTO imChatReqDTO);

  /**
   * 判断是否是文本类消息
   * @param imChatReqDTO
   * @return
   */
  boolean isTextMessage(ImChatReqDTO imChatReqDTO);

  /**
   * 判断是否是图片类消息
   * @param imChatReqDTO
   * @return
   */
  boolean isImageMessage(ImChatReqDTO imChatReqDTO);
}