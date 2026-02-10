package org.cy.micoservice.app.infra.console.config;

import org.cy.micoservice.app.framework.cache.starter.config.RedisKeyBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Component
public class InfraCacheKeyBuilder extends RedisKeyBuilder {

  public String changeRouteConfigKey(String key) {
    return super.buildKey(String.format("blog_infra_console:%s", key));
  }
}