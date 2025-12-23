package org.cy.micoservice.blog.im.facade.router.interfaces;

import org.cy.micoservice.blog.common.base.RpcResponse;
import org.cy.micoservice.blog.im.facade.router.connector.dto.ImSingleMessageDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description: im 通知 facade
 */
public interface ImNotifyFacade {

  /**
   * 发送单条消息给指定用户
   * @param singleMessageDTO
   * @return
   */
  RpcResponse<Boolean> sendSingleMessage(ImSingleMessageDTO singleMessageDTO);
}