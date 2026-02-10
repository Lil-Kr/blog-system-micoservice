package org.cy.micoservice.blog.user.provider.config;

import org.cy.micoservice.blog.framework.cache.starter.config.RedisKeyBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/11/23
 * @Description: user service key prefix
 */
@Component
public class UserRedisKeyBuilder extends RedisKeyBuilder {

  public String buildUserCacheKey(Long userId) {
    return super.buildKey(String.format("user:%s", userId));
  }

}
