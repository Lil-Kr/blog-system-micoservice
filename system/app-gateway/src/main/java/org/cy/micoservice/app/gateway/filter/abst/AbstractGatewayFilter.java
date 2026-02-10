package org.cy.micoservice.app.gateway.filter.abst;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: 抽象网关filter定义
 */
public abstract class AbstractGatewayFilter implements GlobalFilter {

  /**
   * 是否执行本节点 filter 逻辑
   * @param exchange
   * @return
   */
  protected abstract boolean isSupport(ServerWebExchange exchange);

  /**
   * 执行 filter 逻辑
   * @param exchange
   * @param chain
   * @return
   */
  protected abstract Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    if (isSupport(exchange)) {
      return this.doFilter(exchange, chain);
    }
    return chain.filter(exchange);
  }
}