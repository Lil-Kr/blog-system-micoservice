package org.cy.micoservice.blog.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.framework.identiy.starter.response.TokenBodyResponse;
import org.cy.micoservice.blog.gateway.facade.constants.GatewayConstants;
import org.cy.micoservice.blog.gateway.facade.dto.LogRequestDTO;
import org.cy.micoservice.blog.gateway.facade.print.abst.BaseLogPrintStrategy;
import org.cy.micoservice.blog.gateway.filter.abst.AbstractGatewayFilter;
import org.cy.micoservice.blog.gateway.service.LogPrintStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description:
 */
@Slf4j
@Component
public class LogPrintStrategyFilter extends AbstractGatewayFilter implements Ordered {

  @Autowired
  private LogPrintStrategyService logPrintStrategyService;

  @Override
  protected boolean isSupport(ServerWebExchange exchange) {
    LogRequestDTO logRequestDTO = this.getLogRequestDTO(exchange);
    return logPrintStrategyService.hasAvailableStrategy(logRequestDTO);
  }

  @Override
  protected Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
    LogRequestDTO logRequestDTO = this.getLogRequestDTO(exchange);

    List<BaseLogPrintStrategy> availableStrategy = logPrintStrategyService.getAvailableStrategy(logRequestDTO);
    if (CollectionUtils.isNotEmpty(availableStrategy)) {
      /**
       * 统一日志打印, 也可以自定义
       */
      log.info("path: {}, uri: {}, header: {}, userid: {}", logRequestDTO.getPath(), logRequestDTO.getServiceName(), logRequestDTO.getHeaders(), logRequestDTO.getUserId());
    }
    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return GatewayConstants.GatewayOrder.LOG_PRINT_STRATEGY_FILTER_ORDER;
  }

  /**
   * 获取http请求头
   * @param request
   * @return
   */
  private Map<String, List<String>> getHttpHeaders(ServerHttpRequest request) {
    HttpHeaders headers = request.getHeaders();
    Map<String, List<String>> headerMap = new HashMap<>();
    for (String headerName : headers.keySet()) {
      List<String> list = headers.get(headerName);
      headerMap.put(headerName, list);
    }
    return headerMap;
  }

  /**
   * 获取日志打印请求 DTO
   * @param exchange
   * @return
   */
  private LogRequestDTO getLogRequestDTO(ServerWebExchange exchange) {
    ServerHttpRequest request = exchange.getRequest();
    String path = exchange.getRequest().getPath().toString();
    RouteConfig routeConfig = (RouteConfig) exchange.getAttributes().getOrDefault(GatewayConstants.GatewayAttrKey.X_ROUTE, new RouteConfig());
    TokenBodyResponse tokenBodyResponse = (TokenBodyResponse) exchange.getAttributes().getOrDefault(GatewayConstants.GatewayAttrKey.X_JWT_INFO, new TokenBodyResponse());

    LogRequestDTO logRequestDTO = new LogRequestDTO();
    logRequestDTO.setEventTime(System.currentTimeMillis());
    logRequestDTO.setPath(path);
    logRequestDTO.setServiceName(routeConfig.getUri());
    logRequestDTO.setHeaders(getHttpHeaders(request));
    logRequestDTO.setUserId(tokenBodyResponse.getSubject() == null ? null : Long.parseLong(tokenBodyResponse.getSubject()));

    return logRequestDTO;
  }
}
