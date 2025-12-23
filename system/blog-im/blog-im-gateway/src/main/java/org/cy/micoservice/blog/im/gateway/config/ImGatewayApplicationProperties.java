package org.cy.micoservice.blog.im.gateway.config;

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

  @Value("${im.connector.cluster.name:blog-im-connector-cluster}")
  private String imConnectorClusterName;
}