package org.cy.micoservice.blog.framework.cache.starter.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
public class RedisKeyBuilder {

  @Value("${spring.application.name:}")
  public String application;

  public String buildKey(String pattern) {
    return String.format("%s:%s", this.getApplication(), pattern);
  }

  public String getApplication() {
    return application;
  }
}