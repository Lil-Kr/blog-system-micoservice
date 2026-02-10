package org.cy.micoservice.app.im.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description:
 */
@Data
@Configuration
public class ImGatewayApplicationProperties {

  @Value("${spring.cloud.nacos.discovery.server-addr:}")
  private String nacosAddress;

  @Value("${spring.cloud.nacos.username:nacos}")
  private String nacosUsername;

  @Value("${spring.cloud.nacos.password:nacos}")
  private String nacosPassword;

  @Value("${spring.cloud.nacos.discovery.namespace:}")
  private String nacosNamespace;

  @Value("${im.websocket.router.name:websocket-route}")
  private String websocketRouteName;

  @Value("${im.websocket.router.path:/blog/im/**}")
  private String websocketRoutePath;

  /**
   * url根据环境不同进行更换
   */
  @Value("${im.websocket.router.url:ws://localhost:10880/}")
  private String websocketRouteUrl;

  /**
   * 统一管理 im-connector 集群名, 用于 nacos 订阅 im-connector 集群下线事件
   * blog-im-connector-cluster
   */
  @Value("${im.connector.cluster.name:}")
  private String imConnectorClusterName;
}