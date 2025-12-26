package org.cy.micoservice.blog.message.provider.service.impl;

import org.cy.micoservice.blog.common.base.PageResponse;
import org.cy.micoservice.blog.entity.message.model.provider.req.ChatRelationPageReq;
import org.cy.micoservice.blog.entity.message.model.provider.resp.ChatRelationResp;
import org.cy.micoservice.blog.message.facade.dto.req.ChatRelationPageReqDTO;
import org.cy.micoservice.blog.message.facade.dto.resp.ChatRelationRespDTO;
import org.cy.micoservice.blog.message.provider.config.MessageCacheKeyBuilder;
import org.cy.micoservice.blog.message.provider.dao.es.ChatRelationEsMapper;
import org.cy.micoservice.blog.message.provider.service.ChatRelationEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 对话关系 es service
 */
@Service
public class ChatRelationEsServiceImpl implements ChatRelationEsService {

  @Autowired
  private ChatRelationEsMapper chatRelationEsMapper;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Autowired
  private MessageCacheKeyBuilder messageCacheKeyBuilder;

  @Override
  public PageResponse<ChatRelationRespDTO> listChatRelationFromPage(ChatRelationPageReqDTO request) {
    PageResponse<ChatRelationRespDTO> chatRelationsPageResp = chatRelationEsMapper.listChatRelationFromPage(request);

    List<ChatRelationRespDTO> chatRelationRespDTOList = chatRelationsPageResp.getDataList();
    if (CollectionUtils.isEmpty(chatRelationRespDTOList)) {
      return chatRelationsPageResp;
    }
    // 顺带将消息总数给放入缓存中, 减少后续im处理下游的压力
    String cacheKey = messageCacheKeyBuilder.chatRelationMsgCountKey(request.getUserId());
    for (ChatRelationRespDTO chatRelationRespDTO : chatRelationRespDTOList) {
      Long msgCount = chatRelationRespDTO.getMsgCount();
      if (msgCount == null) continue;

      // 消息总数传递进去
      redisTemplate.opsForHash().put(cacheKey, chatRelationRespDTO.getRelationId(), msgCount);
    }
    // 过期时间: 24h
    redisTemplate.expire(cacheKey, 24, TimeUnit.HOURS);
    return chatRelationsPageResp;
  }
}
