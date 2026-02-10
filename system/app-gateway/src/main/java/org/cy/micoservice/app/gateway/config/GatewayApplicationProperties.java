package org.cy.micoservice.app.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2026/1/3
 * @Description: 网关服务 properties 配置
 */
@Data
@Configuration
public class GatewayApplicationProperties {

  @Value("${spring.application.name:}")
  private String appName;

  @Value("${spring.cloud.nacos.discovery.server-addr:}")
  private String serverAddr;

  @Value("${spring.cloud.nacos.discovery.namespace:}")
  private String namespace;

  @Value("${spring.cloud.nacos.username:}")
  private String username;

  @Value("${spring.cloud.nacos.password:}")
  private String password;

  @Value("${blog.gateway.refresh.data-id:}")
  private String refreshDataId;

  @Value("${blog.gateway.refresh.group:}")
  private String refreshGroup;

  @Value("${decrypty.secret-key:}")
  private String decryptSecretKey;
}