package org.cy.micoservice.blog.message.provider.service;

import org.cy.micoservice.blog.entity.message.model.provider.pojo.es.ChatBoxEs;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReqDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 收信箱 service
 */
public interface ChatBoxEsService {

  /**
   * 写入数据
   * @param chatBoxEs
   * @return
   */
  boolean index(ChatBoxEs chatBoxEs);

  /**
   * 批量更新收件箱信息
   * @param imChatReqDTOMap
   * @return
   */
  boolean bulkMap(Map<String, ImChatReqDTO> imChatReqDTOMap);

  /**
   *
   * @param chatBoxEsList
   * @return
   */
  boolean bulkList(List<ChatBoxEs> chatBoxEsList);

  /**
   * 单个查询
   * @param userId
   * @param relationId
   * @return
   */
  ChatBoxEs get(Long userId, Long relationId);

  /**
   * 支持批量查询用户的已读offset值
   * @param userId
   * @return
   */
  List<ChatBoxEs> listByUserId(Long userId);
}