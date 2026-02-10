package org.cy.micoservice.app.gateway.filter;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.common.base.api.WebResponse;
import org.cy.micoservice.app.gateway.facade.constants.GatewayConstants;
import org.cy.micoservice.app.gateway.filter.abst.AbstractGatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: (5). 异常处理器 filter
 */
@Slf4j
@Component
public class ExceptionHandlerFilter extends AbstractGatewayFilter implements Ordered {

  @Override
  protected boolean isSupport(ServerWebExchange exchange) {
    Object errorCode = exchange.getAttributes().get(GatewayConstants.GatewayAttrKey.X_ROUTE_ERROR_CODE);
    Object errorMsg = exchange.getAttributes().get(GatewayConstants.GatewayAttrKey.X_ROUTE_ERROR_MSG);
    return Objects.nonNull(errorCode) && Objects.nonNull(errorMsg);
  }

  @Override
  protected Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String errorCode = (String) exchange.getAttributes().get(GatewayConstants.GatewayAttrKey.X_ROUTE_ERROR_CODE);
    String errorMsg = (String) exchange.getAttributes().get(GatewayConstants.GatewayAttrKey.X_ROUTE_ERROR_MSG);
    WebResponse<Object> error = WebResponse.busError(Integer.parseInt(errorCode), errorMsg);
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.BAD_GATEWAY);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    return response.writeWith(
      Mono.fromSupplier(() -> {
        DataBufferFactory factory = exchange.getResponse().bufferFactory();
        return factory.wrap(JSON.toJSONBytes(error));
      })
    );
  }

  @Override
  public int getOrder() {
    return GatewayConstants.GatewayOrder.EXCEPTION_HANDLER_FILTER_ORDER;
  }
}