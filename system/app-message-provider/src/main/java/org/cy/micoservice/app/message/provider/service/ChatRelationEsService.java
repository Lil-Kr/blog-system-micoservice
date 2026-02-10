package org.cy.micoservice.app.message.provider.service;

import org.cy.micoservice.app.common.base.provider.PageResponseDTO;
import org.cy.micoservice.app.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.app.message.facade.dto.req.ChatRelationReqDTO;
import org.cy.micoservice.app.message.facade.dto.req.im.ImChatReqDTO;
import org.cy.micoservice.app.message.facade.dto.resp.ChatRelationRespDTO;

import java.util.List;
import java.util.Map;

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

  boolean edit(ChatRelationReqDTO chatRelationReqDTO);

  /**
   * 批量保存会话关系数据
   * @param imChatReqDTOMap
   * @return
   */
  boolean bulkMap(Map<String, ImChatReqDTO> imChatReqDTOMap);

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

  /**
   * 根据用户id查询所有会话记录
   * @param userId
   * @return
   */
  List<ChatRelationRespDTO> listByUserIdOrReceiverId(Long userId);
}