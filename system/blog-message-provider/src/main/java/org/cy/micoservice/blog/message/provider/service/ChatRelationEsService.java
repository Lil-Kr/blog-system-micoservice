package org.cy.micoservice.blog.message.provider.service;

import org.cy.micoservice.blog.common.base.provider.PageResponseDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 对话关系es服务
 */
public interface ChatRelationEsService {

  /**
   * 新增对话关系
   * @param chatRelationReqDTO
   */
  boolean add(ChatRelationReqDTO chatRelationReqDTO);

  /**
   * 批量保存会话关系数据
   */
  boolean bulk(List<ChatRelationReqDTO> chatRelationReqDTOList);

  /**
   * 分页查询对话关系列表
   * @param request
   * @return
   */
  PageResponseDTO<ChatRelationRespDTO> listChatRelationFromPage(ChatRelationPageReqDTO request);

  /**
   * 查询单个会话关系
   * @param chatRelationPageReqDTO
   * @return
   */
  ChatRelationRespDTO queryRelationInfo(ChatRelationPageReqDTO chatRelationPageReqDTO);
}