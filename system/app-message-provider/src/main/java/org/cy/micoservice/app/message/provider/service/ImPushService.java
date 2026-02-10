package org.cy.micoservice.app.message.provider.service;


import org.cy.micoservice.app.message.facade.dto.req.im.ImChatReqDTO;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 推送消息给router层
 */
public interface ImPushService {

  /**
   * 推送单条消息到router服务消费的mq中
   * @param imChatReqDTO
   */
  void singlePushRouterMessage(ImChatReqDTO imChatReqDTO);

  /**
   * 批量推送消息到mq
   * @param imChatReqDTOList
   */
  void batchPushRouterMessage(List<ImChatReqDTO> imChatReqDTOList);
}