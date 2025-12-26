package org.cy.micoservice.blog.message.provider.config;

import org.cy.micoservice.blog.framework.cache.starter.config.RedisKeyBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Component
public class MessageCacheKeyBuilder extends RedisKeyBuilder {

  public String chatRelationMsgCountKey(Long userId) {
    return super.buildKey(String.format("chat_relation:msg_count:%s", userId));
  }
}