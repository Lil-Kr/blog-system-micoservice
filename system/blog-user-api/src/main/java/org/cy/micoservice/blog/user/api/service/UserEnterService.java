package org.cy.micoservice.blog.user.api.service;

import org.cy.micoservice.blog.user.facade.provider.req.UserEnterInitReqDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/31
 * @Description: 用户进入程序事件信号 service
 */
public interface UserEnterService {

  /**
   * 发送MQ通知, 用户进入程序时触发
   * @param userEnterInitReqDTO
   */
  boolean enter(UserEnterInitReqDTO req);
}