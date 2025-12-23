package org.cy.micoservice.blog.infra.console.service.impl;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.gateway.facade.utils.NacosRouteVersionUtils;
import org.cy.micoservice.blog.infra.console.service.NacosService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
@Slf4j
@Service
public class NacosServiceImpl implements NacosService, InitializingBean, DisposableBean {

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

  private ConfigService configService;

  @Override
  public Long incrVersion() throws NacosException {
    try {
      String config = configService.getConfig(refreshDataId, refreshGroup, 5000);
      long version = NacosRouteVersionUtils.parseConfigVersionFromConfig(config);
      /**
       * 这里有并发问题, 防止多个线程同时更新 version + 1
       */
      version = version + 1;
      String finalString = NacosRouteVersionUtils.formatConfigVersion(version);
      boolean published = configService.publishConfig(refreshDataId, refreshGroup, finalString, "properties");

      log.info("publish status: {}", published);
      configService.shutDown();
      return version;
    } catch (Exception e) {
      log.error("nacos config error: {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Properties properties = new Properties();
    properties.put("serverAddr", serverAddr);
    properties.put("namespace", namespace);
    properties.put("username", username);
    properties.put("password", password);
    configService = NacosFactory.createConfigService(properties);
  }

  @Override
  public void destroy() throws Exception {
    if (configService != null) {
      configService.shutDown();
    }
  }
}
