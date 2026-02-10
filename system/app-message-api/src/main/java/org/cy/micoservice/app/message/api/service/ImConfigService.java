package org.cy.micoservice.app.message.api.service;


import org.cy.micoservice.app.entity.message.model.provider.resp.ImConfigResp;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
public interface ImConfigService {

  /**
   * 获取im服务器的配置
   * @return
   */
  ImConfigResp getImChatConfig(Long userId);
}
