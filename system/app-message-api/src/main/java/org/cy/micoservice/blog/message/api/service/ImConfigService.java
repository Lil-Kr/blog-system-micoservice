package org.cy.micoservice.blog.message.api.service;


import org.cy.micoservice.blog.entity.message.model.provider.resp.ImConfigResp;

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
