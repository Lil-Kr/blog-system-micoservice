package org.cy.micoservice.app.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.gateway.facade.constants.GatewayConstants;
import org.cy.micoservice.app.gateway.filter.abst.AbstractGatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: (2). http 参数解析
 */
@Slf4j
@Component
public class HttpParameterResolverFilter extends AbstractGatewayFilter implements Ordered {

  /**
   * 所有http请求的参数都要解析
   * @param exchange
   * @return
   */
  @Override
  protected boolean isSupport(ServerWebExchange exchange) {
    return true;
  }

  /**
   * 使用异步IO模型
   * @param exchange
   * @param chain
   * @return
   */
  @Override
  protected Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    HttpCookie httpCookie = request.getCookies().getFirst(GatewayConstants.AUTH_HEADER_NAME);
    if (httpCookie != null) {
      String authorization = httpCookie.getValue();
      exchange.getAttributes().put(GatewayConstants.GatewayAttrKey.X_AUTHORIZATION, authorization);
    }
    Flux<DataBuffer> dataBufferFlux = request.getBody();

    return  DataBufferUtils
      .join(dataBufferFlux)
      // if dataBufferFlux is null, will be block
      .defaultIfEmpty(exchange.getResponse().bufferFactory().allocateBuffer(0))
      .flatMap(dataBuffer -> {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);
        // 所有请求参数的内容
        String requestBody = new String(bytes, StandardCharsets.UTF_8);
        exchange.getAttributes().put(GatewayConstants.GatewayAttrKey.X_REQUEST_BODY, requestBody);

        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
          DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
          return Mono.just(buffer);
        });

        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(request) {
          @Override
          public Flux<DataBuffer> getBody() {
            return cachedFlux;
          }
        };
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    });
  }

  @Override
  public int getOrder() {
    return GatewayConstants.GatewayOrder.HTTP_PARAMETER_RESOLVER_FILTER_ORDER;
  }
}
