package org.cy.micoservice.blog.infra.console.config;

import org.cy.micoservice.blog.framework.cache.starter.config.RedisKeyBuilder;
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