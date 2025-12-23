package org.cy.micoservice.blog.message.provider.service;


import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReq;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 推送消息给router层
 */
public interface ImPushService {

  /**
   * 推送单条消息到router服务消费的mq中
   * @param imChatReq
   */
  void pushRouterSingleMessage(ImChatReq imChatReq);
}