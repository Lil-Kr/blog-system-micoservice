package org.cy.micoservice.blog.gateway.filter;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.security.impl.AES128GCMCrypto;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.common.constants.gateway.GatewayHeadersConstants;
import org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterAuthTypeEnum;
import org.cy.micoservice.blog.framework.identiy.starter.response.TokenBodyResponse;
import org.cy.micoservice.blog.framework.identiy.starter.template.AuthTemplate;
import org.cy.micoservice.blog.gateway.facade.constants.GatewayConstants;
import org.cy.micoservice.blog.gateway.filter.abst.AbstractGatewayFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: 统一认证鉴权 filter
 */
@Slf4j
@Component
public class JWTAuthFilter extends AbstractGatewayFilter implements InitializingBean, Ordered {

  @Autowired
  private AuthTemplate authTemplate;

  private AES128GCMCrypto aes128GCMCrypto;

  /**
   * 过滤不需要鉴权的api, 根据 RouteConfig 里面的标识进行判断
   * @param exchange
   * @return
   */
  @Override
  protected boolean isSupport(ServerWebExchange exchange) {
    RouteConfig routeConfig = exchange.getAttribute(GatewayConstants.GatewayAttrKey.X_ROUTE);
    if (Objects.isNull(routeConfig) || ! GatewayRouterAuthTypeEnum.JWT.getCode().equals(routeConfig.getAuthType())) return false;
    return exchange.getAttributes().get(GatewayConstants.GatewayAttrKey.X_AUTHORIZATION) != null;
  }

  @Override
  protected Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String authorization = (String) exchange.getAttributes().get(GatewayConstants.GatewayAttrKey.X_AUTHORIZATION);
    boolean isTokenValid = authTemplate.checkToken(authorization);
    if (!isTokenValid) {
      exchange.getAttributes().put(GatewayConstants.GatewayAttrKey.X_ROUTE_ERROR_CODE, "500");
      exchange.getAttributes().put(GatewayConstants.GatewayAttrKey.X_ROUTE_ERROR_MSG, "invalid authorization");
      return chain.filter(exchange);
    }
    TokenBodyResponse tokenBodyResponse = authTemplate.extraAttrsFromToken(authorization);
    String requestBlogServiceToken = this.createTokenBodyJson(authorization);
    exchange.getAttributes().put(GatewayConstants.GatewayAttrKey.X_JWT_INFO, tokenBodyResponse);
    exchange.getRequest().mutate().header(GatewayHeadersConstants.X_GATEWAY_IDENTIFY, requestBlogServiceToken);
    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return GatewayConstants.GatewayOrder.AUTH_FILTER_ORDER;
  }

  /**
   * 创建传递给下游的json对象体
   * @param authorization
   * @return
   */
  private String createTokenBodyJson(String authorization) {
    TokenBodyResponse tokenBodyResponse = authTemplate.extraAttrsFromToken(authorization);
    String userId = authTemplate.getSubjectFromToken(authorization);
    String phone = (String) tokenBodyResponse.getAuthAttrs().getOrDefault("phone", StringUtils.EMPTY);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("userId", userId);
    jsonObject.put("phone", phone);
    String jsonStr = jsonObject.toJSONString();
    try {
      return aes128GCMCrypto.encrypt(jsonStr);
    } catch (Exception e) {
      log.error("JWTAuthFilter encrypt error:", e);
    }
    return null;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.aes128GCMCrypto = new AES128GCMCrypto("PxMNarWuqoNFFGJ5QGgesg==");
  }
}
