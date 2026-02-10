package org.cy.micoservice.app.im.router.service;

import org.cy.micoservice.app.im.facade.dto.router.ImSingleMessageDTO;

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