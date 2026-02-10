package org.cy.micoservice.app.infra.console.sdk.config;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.infra.console.sdk.core.InfraConsoleClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/5
 * @Description: 控制台sdk自动初始化配置类
 */
@Slf4j
public class InfraConsoleSdkAutoConfiguration {

  @Bean
  public SdkProperties sdkProperties() {
    return new SdkProperties();
  }

  @Bean
  @ConditionalOnBean(SdkProperties.class)
  public InfraConsoleClient infraConsoleClient(SdkProperties sdkProperties) throws Exception {
    InfraConsoleClient client = new InfraConsoleClient(sdkProperties);
    client.init();
    return client;
  }
}