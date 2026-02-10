package org.cy.micoservice.app.im.facade.interfaces;

import org.cy.micoservice.app.common.base.provider.RpcResponse;
import org.cy.micoservice.app.im.facade.dto.router.ImSingleMessageDTO;

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