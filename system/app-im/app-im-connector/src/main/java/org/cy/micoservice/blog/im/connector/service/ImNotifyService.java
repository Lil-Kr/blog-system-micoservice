package org.cy.micoservice.blog.im.connector.service;

import org.cy.micoservice.blog.im.facade.dto.connector.ImMessageDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description: im主动推送service
 */
public interface ImNotifyService {

  /**
   * 主动推送消息给指定用户id
   * @param userId
   * @param dto
   * @return
   */
  boolean sendMsgByUserId(Long userId, ImMessageDTO dto);

}