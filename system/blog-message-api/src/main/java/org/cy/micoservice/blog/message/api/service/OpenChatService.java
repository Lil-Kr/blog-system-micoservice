package org.cy.micoservice.blog.message.api.service;

import org.cy.micoservice.blog.entity.message.model.provider.req.OpenChatReq;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 用户开启会话 service
 */
public interface OpenChatService {

  /**
   * 上报数据信息
   */
  boolean reportInfo(OpenChatReq openChatReq);
}
