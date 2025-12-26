package org.cy.micoservice.blog.message.facade.interfaces;

import org.cy.micoservice.blog.common.base.PageResponse;
import org.cy.micoservice.blog.common.base.RpcResponse;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
public interface ChatRelationFacade {

  /**
   * 新增对话关系
   * @param chatRelationReqDTO
   */
  RpcResponse<Boolean> add(ChatRelationReqDTO chatRelationReqDTO);

  /**
   * 分页查询用户关系列表
   * @param chatRelationPageReqDTO
   * @return
   */
  RpcResponse<PageResponse<ChatRelationRespDTO>> queryInPage(ChatRelationPageReqDTO chatRelationPageReqDTO);

  /**
   * 更新对话关系信息
   * @param chatRelationReqDTO
   */
  RpcResponse<Boolean> updateRelationByRelationId(ChatRelationReqDTO chatRelationReqDTO);

  /**
   * 查询对话关系信息
   * @param chatRelationPageReqDTO
   * @return
   */
  RpcResponse<ChatRelationRespDTO> queryRelationInfo(ChatRelationPageReqDTO chatRelationPageReqDTO);

  /**
   * 发送对话并且更新对话关系
   * @param chatRelationReqDTO
   * @return
   */
  RpcResponse<Boolean> addRecordAndUpdateRelation(ChatRelationReqDTO chatRelationReqDTO);
}