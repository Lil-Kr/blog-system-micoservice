package org.cy.micoservice.app.message.provider.config;

import org.cy.micoservice.app.framework.cache.starter.config.RedisKeyBuilder;
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

  public String chatRelationSeqNoKey(String relationId) {
    return super.buildKey(String.format("chat_relation:seq_no:%s", relationId));
  }

  public String buildChatBoxSeqNoKey(Long userId) {
    return super.buildKey(String.format("chat_box:seq_no:%s", userId));
  }

  public String buildChatSeqZSetKey(Long userId, String relationId) {
    return super.buildKey(String.format("chat_record:chat_seq:%s:%s", userId, relationId));
  }

  public String buildChatRecordKey(Long userId, String relationId) {
    return super.buildKey(String.format("chat_record:%s:%s", userId, relationId));
  }
}