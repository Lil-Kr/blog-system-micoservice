package org.cy.micoservice.blog.infra.console.sdk.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/5
 * @Description:
 */
@Data
public class SdkProperties {

  @Value("${infra.console.sdk.nacos.address:}")
  private String nacosAddress;

  @Value("${infra.console.sdk.nacos.namespace:}")
  private String nacosNamespace;

  @Value("${infra.console.sdk.nacos.user:}")
  private String nacosUser;

  @Value("${infra.console.sdk.nacos.pwd:}")
  private String nacosPwd;

  @Value("${infra.console.service-name:}")
  private String infraConsoleServiceName;

  @Value("${infra.console.service-group:}")
  private String infraConsoleServiceGroup;

  @Value("${spring.application.name:unknow}")
  private String clientName;

}
