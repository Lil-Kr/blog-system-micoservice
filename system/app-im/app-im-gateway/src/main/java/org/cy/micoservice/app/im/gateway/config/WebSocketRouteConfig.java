package org.cy.micoservice.app.im.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private ImGatewayApplicationProperties applicationProperties;

  @Bean
  public RouteLocator websocketRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
      .route(applicationProperties.getWebsocketRouteName(),
        r -> r
          .path(applicationProperties.getWebsocketRoutePath())
          .uri(applicationProperties.getWebsocketRouteUrl()))
      .build();
  }
}