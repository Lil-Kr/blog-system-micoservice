package org.cy.micoservice.blog.im.gateway.config;

import org.cy.micoservice.blog.im.gateway.contstants.WebSocketGatewayConstants;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description:
 */
@Configuration
public class WebSocketRouteConfig {

  @Bean
  public RouteLocator websocketRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
      .route(WebSocketGatewayConstants.WEBSOCKET_ROUTE_NAME,
        r -> r
          .path(WebSocketGatewayConstants.WEBSOCKET_ROUTE_PATH)
          .uri(WebSocketGatewayConstants.WEBSOCKET_ROUTE_URL))
      .build();
  }
}