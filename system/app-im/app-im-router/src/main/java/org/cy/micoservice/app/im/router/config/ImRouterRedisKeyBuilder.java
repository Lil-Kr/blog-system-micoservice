package org.cy.micoservice.app.im.router.config;

import org.cy.micoservice.app.framework.cache.starter.config.RedisKeyBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/16
 * @Description: im router layer redis key builder
 */
@Component
public class ImRouterRedisKeyBuilder extends RedisKeyBuilder {

  public String imUserConnectorKey(String userId) {
    return super.buildKey(String.format("im_connector:user:%s", userId));
  }
}