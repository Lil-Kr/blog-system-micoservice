package org.cy.micoservice.blog.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @Author: Lil-K
 * @Date: 2025/12/7
 * @Description: 根据 gateway 的请求路径进行限流配置
 */
@Configuration
public class RateLimitConfig {

  /**
   * 其他示例: 按请求路径限流
   * @return
   */
  @Bean
  public KeyResolver pathKeyResolver() {
    return exchange -> Mono.just(exchange.getRequest().getPath().value());
  }
}