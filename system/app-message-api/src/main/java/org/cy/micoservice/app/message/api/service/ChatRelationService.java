package org.cy.micoservice.app.message.api.service;


import org.cy.micoservice.app.common.base.provider.PageResponseDTO;
import org.cy.micoservice.app.entity.message.model.provider.req.ChatRelationPageReq;
import org.cy.micoservice.app.entity.message.model.provider.req.ChatRelationReq;
import org.cy.micoservice.app.entity.message.model.provider.resp.ChatRelationResp;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
public interface ChatRelationService {

  /**
   * 新增对话关系
   * @param chatRelationReq
   */
  boolean add(ChatRelationReq chatRelationReq);

  /**
   * 分页查询用户关系列表
   * @param chatRelationPageReq
   * @return
   */
  PageResponseDTO<ChatRelationResp> pageChatRelationList(ChatRelationPageReq chatRelationPageReq);

  /**
   * 更新对话关系信息
   * @param chatRelationReq
   */
  void updateRelationByRelationId(ChatRelationReq chatRelationReq);

  /**
   * 查询对话关系信息
   * @param chatRelationPageReq
   * @return
   */
  ChatRelationResp getRelationInfo(ChatRelationPageReq chatRelationPageReq);
}
