package org.cy.micoservice.blog.message.provider.service;

import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 对话关系service
 */
public interface ChatRelationService {

  /**
   * 新增会话关系
   * @param chatRelationReqDTO
   */
  boolean add(ChatRelationReqDTO chatRelationReqDTO);

  /**
   * 分页查询用户关系列表
   * @param chatRelationPageReqDTO
   * @return
   */
  PageResponseDTO<ChatRelationRespDTO> queryInPage(ChatRelationPageReqDTO chatRelationPageReqDTO);

  /**
   * 更新对话关系信息
   * @param chatRelationReqDTO
   */
  boolean updateRelationByRelationId(ChatRelationReqDTO chatRelationReqDTO);


  /**
   * 查询对话关系信息
   * @param chatRelationPageReqDTO
   * @return
   */
  ChatRelationRespDTO queryRelationInfo(ChatRelationPageReqDTO chatRelationPageReqDTO);

  /**
   * 添加对话并且更新对话关系
   * @param chatRelationReqDTO
   * @return
   */
  boolean addRecordAndUpdateRelation(ChatRelationReqDTO chatRelationReqDTO);
}