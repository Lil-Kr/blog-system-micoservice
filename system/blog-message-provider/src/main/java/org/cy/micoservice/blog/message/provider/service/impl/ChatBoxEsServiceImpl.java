package org.cy.micoservice.blog.message.provider.service.impl;

import jakarta.annotation.Resource;
import org.cy.micoservice.blog.entity.message.model.provider.po.ChatBoxEs;
import org.cy.micoservice.blog.message.provider.dao.es.ChatBoxEsMapper;
import org.cy.micoservice.blog.message.provider.service.ChatBoxEsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 收信箱 service impl
 */
@Service
public class ChatBoxEsServiceImpl implements ChatBoxEsService {

  @Resource
  private ChatBoxEsMapper chatBoxEsMapper;

  @Override
  public boolean index(ChatBoxEs chatBoxEs) {
    return chatBoxEsMapper.index(chatBoxEs);
  }

  @Override
  public boolean bulk(List<ChatBoxEs> chatBoxEsList) {
    return chatBoxEsMapper.bulk(chatBoxEsList);
  }

  @Override
  public ChatBoxEs get(Long userId, Long relationId) {
    return chatBoxEsMapper.get(userId,relationId);
  }
}
