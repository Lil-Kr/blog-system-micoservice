package org.cy.micoservice.blog.infra.console.service.impl.route;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.gateway.facade.utils.NacosRouteVersionUtils;
import org.cy.micoservice.blog.infra.console.config.InfraApplicationProperties;
import org.cy.micoservice.blog.infra.console.service.NacosService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description: nacos 对网关版本配置文件做修改
 */
@Slf4j
@Service
public class NacosServiceImpl implements NacosService, InitializingBean, DisposableBean {

  @Autowired
  private InfraApplicationProperties applicationProperties;

  // nacos config
  private ConfigService configService;

  @Override
  public Long incrVersion() throws NacosException {
    try {
      String config = configService.getConfig(applicationProperties.getRefreshDataId(), applicationProperties.getRefreshGroup(), 5000);
      long version = NacosRouteVersionUtils.parseConfigVersionFromConfig(config);
      /**
       * 这里有并发问题, 防止多个线程同时更新 version + 1
       */
      version = version + 1;
      String finalString = NacosRouteVersionUtils.formatConfigVersion(version);
      boolean published = configService.publishConfig(applicationProperties.getRefreshDataId(), applicationProperties.getRefreshGroup(), finalString, "properties");

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
    properties.put("serverAddr", applicationProperties.getServerAddr());
    properties.put("namespace", applicationProperties.getNamespace());
    properties.put("username", applicationProperties.getUsername());
    properties.put("password", applicationProperties.getPassword());
    configService = NacosFactory.createConfigService(properties);
  }

  @Override
  public void destroy() throws Exception {
    if (configService != null) {
      configService.shutDown();
    }
  }
}
