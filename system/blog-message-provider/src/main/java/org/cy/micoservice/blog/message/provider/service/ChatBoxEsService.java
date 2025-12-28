package org.cy.micoservice.blog.message.provider.service;

import org.cy.micoservice.blog.entity.message.model.provider.po.ChatBoxEs;

import java.util.List;

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
   * @param chatBoxEsList
   * @return
   */
  boolean bulk(List<ChatBoxEs> chatBoxEsList);

  /**
   * 单个查询
   * @param userId
   * @param relationId
   * @return
   */
  ChatBoxEs get(Long userId, Long relationId);
}