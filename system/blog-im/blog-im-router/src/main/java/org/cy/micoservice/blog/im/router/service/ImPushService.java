package org.cy.micoservice.blog.im.router.service;

import org.cy.micoservice.blog.im.facade.connector.dto.ImSingleMessageDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description: im消息推送service
 */
public interface ImPushService {

  /**
   * 发送单条消息给指定人
   *
   * @param imSingleMessageDTO
   * @return
   */
  void sendSingleMsgToObject(ImSingleMessageDTO imSingleMessageDTO);
}