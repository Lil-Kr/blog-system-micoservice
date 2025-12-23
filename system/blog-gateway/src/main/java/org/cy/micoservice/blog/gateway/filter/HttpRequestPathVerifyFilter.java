package org.cy.micoservice.blog.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.gateway.constants.GatewayConstants;
import org.cy.micoservice.blog.gateway.filter.abst.AbstractGatewayFilter;
import org.cy.micoservice.blog.gateway.service.RouteCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: Lil-K
 * @Date: 2025/11/28
 * @Description: http请求转发合法性校验
 */
@Slf4j
@Component
public class HttpRequestPathVerifyFilter extends AbstractGatewayFilter implements Ordered {

  @Autowired
  private RouteCacheService routeCacheService;

  @Override
  protected boolean isSupport(ServerWebExchange exchange) {
    return true;
  }

  @Override
  protected Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
    // 获取请求类型
    ServerHttpRequest httpRequest = exchange.getRequest();
    // 获取请求路径
    String requestPath = httpRequest.getPath().toString();
    // 获取调用方法
    String method = httpRequest.getMethod().name();
    log.info("requestPath: {}, method: {}", requestPath, method);

    RouteConfig routeConfig = routeCacheService.get(method, requestPath);
    if (routeConfig == null) {
      exchange.getAttributes().put(GatewayConstants.GatewayAttrKey.X_ROUTE_ERROR_CODE, "404");
      exchange.getAttributes().put(GatewayConstants.GatewayAttrKey.X_ROUTE_ERROR_MSG, "invalid path");
    } else {
      exchange.getAttributes().put(GatewayConstants.GatewayAttrKey.X_ROUTE, routeConfig);
    }
    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return GatewayConstants.GatewayOrder.HTTP_REQUEST_PATH_VALID_FILTER_ORDER;
  }
}