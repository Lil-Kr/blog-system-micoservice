package org.cy.micoservice.blog.gateway.filter;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.service.GenericService;
import org.cy.micoservice.blog.common.constants.gateway.GatewayInfraConsoleSdkConstants;
import org.cy.micoservice.blog.common.utils.JsonUtil;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterSchemaEnum;
import org.cy.micoservice.blog.gateway.facade.constants.GatewayConstants;
import org.cy.micoservice.blog.gateway.filter.abst.AbstractGatewayFilter;
import org.cy.micoservice.blog.gateway.service.DubboInvokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: (6). dubbo 路由转发 filter
 */
@Slf4j
@Component
public class DubboInvokeFilter extends AbstractGatewayFilter implements Ordered {

  @Autowired
  private DubboInvokerService dubboInvokerService;

  @Override
  protected boolean isSupport(ServerWebExchange exchange) {
    Object obj = exchange.getAttributes().get(GatewayConstants.GatewayAttrKey.X_ROUTE);
    if (Objects.isNull(obj)) return false;
    RouteConfig routeConfig = (RouteConfig) obj;
    return GatewayRouterSchemaEnum.DUBBO.getCode().equals(routeConfig.getSchema());
  }

  @Override
  protected Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
    RouteConfig routeConfig = (RouteConfig)exchange.getAttributes().get(GatewayConstants.GatewayAttrKey.X_ROUTE);
    return this.dubboInvoke(exchange, routeConfig);
  }

  @Override
  public int getOrder() {
    return GatewayConstants.GatewayOrder.DUBBO_INVOKE_FILTER_ORDER;
  }

  /**
   * dubbo 路由转发
   * @param exchange
   * @param routeConfig
   * @return
   */
  private Mono<Void> dubboInvoke(ServerWebExchange exchange, RouteConfig routeConfig) {
    String dobbuUri = routeConfig.getUri();
    dobbuUri = dobbuUri.replaceAll(GatewayInfraConsoleSdkConstants.DUBBO_URL_PREFIX, "");
    String[] dobbUriArray = dobbuUri.split("#");
    log.info("dobbuUri: {}, dobbUriArray: {}", dobbUriArray[0], dobbUriArray[1]);
    GenericService genericService = dubboInvokerService.get(dobbUriArray[0]);

    Object[] params = this.getDubboInvokeParam(exchange);

    /**
     * 重点: 转发调用 dubbo 服务api
     */
    Object result = genericService.$invoke(dobbUriArray[1], new String[]{routeConfig.getDubboInvokeParamClass()}, params);
    ServerHttpResponse response = exchange.getResponse();
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    return exchange.getResponse().writeWith(
      Mono.fromSupplier(() -> {
        DataBufferFactory factory = exchange.getResponse().bufferFactory();
        return factory.wrap(JSON.toJSONBytes(result));
      })
    );
  }

  private Object[] getDubboInvokeParam(ServerWebExchange exchange) {
    Object requestBody = exchange.getAttributes().get(GatewayConstants.GatewayAttrKey.X_REQUEST_BODY);
    if (Objects.nonNull(requestBody)) {
      String requestBodyJson = requestBody.toString();
      Map<String, Object> paramMap = JsonUtil.objectToMap(requestBodyJson);
      return new Object[]{paramMap};
    }
    return new Object[]{};
  }
}
