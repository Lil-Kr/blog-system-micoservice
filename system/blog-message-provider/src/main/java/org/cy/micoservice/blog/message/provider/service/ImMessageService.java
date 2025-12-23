package org.cy.micoservice.blog.message.provider.service;

import org.cy.micoservice.blog.audit.facade.dto.AuditResultMessageDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReq;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: im消息处理
 */
public interface ImMessageService {

  /**
   * 发送审核mq消息 (兜底逻辑)
   * @param imChatReq
   * @return
   */
  boolean sendAuditMessageToMQ(ImChatReq imChatReq);

  /**
   * rpc获取文本审核结果 (更快速)
   * @param imChatReq
   * @return
   */
  AuditResultMessageDTO getTextAuditMessageResult(ImChatReq imChatReq);

  /**
   * 判断是否是文本类消息
   * @param imChatReq
   * @return
   */
  boolean isTextMessage(ImChatReq imChatReq);

  /**
   * 判断是否是图片类消息
   * @param imChatReq
   * @return
   */
  boolean isImageMessage(ImChatReq imChatReq);
}