package org.cy.micoservice.blog.message.provider.service;

import org.cy.micoservice.blog.common.base.PageResponse;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 对话关系es服务
 */
public interface ChatRelationEsService {

  /**
   * 分页查询对话关系
   * @param request
   * @return
   */
  PageResponse<ChatRelationRespDTO> listChatRelationFromPage(ChatRelationPageReqDTO request);
}