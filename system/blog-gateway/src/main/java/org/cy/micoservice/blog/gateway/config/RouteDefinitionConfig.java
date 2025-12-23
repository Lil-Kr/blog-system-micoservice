package org.cy.micoservice.blog.gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.constants.gateway.GatewayInfraConsoleSdkConstants;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.entity.gateway.model.enums.GatewayRouterSchemaEnum;
import org.cy.micoservice.blog.gateway.service.DubboInvokeService;
import org.cy.micoservice.blog.gateway.service.RouteCacheService;
import org.cy.micoservice.blog.gateway.service.RouteConfigService;
import org.cy.micoservice.blog.gateway.service.RouteDefinitionWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/11/23
 * @Description: gateway 网关路由定义配置
 */
@Slf4j
@Configuration
public class RouteDefinitionConfig {

  @Autowired
  private RouteDefinitionWriterService routeDefinitionWriterService;

  @Autowired
  private RouteConfigService routerConfigService;

  @Autowired
  private RouteCacheService routeCacheService;

  @Autowired
  private DubboInvokeService dubboInvokeService;

  @PostConstruct
  public void initRouteDefinition() {
    log.info("loading route config from db start");
    List<RouteConfig> routeConfigList = routerConfigService.routeConfigAllValidaList();
    initDubboInvoke();
    for (RouteConfig routeConfig : routeConfigList) {
      routeDefinitionWriterService.save(routeConfig);
      routeCacheService.put(routeConfig);

      // init dubbo <uri, dubboInvokeService>
      if (GatewayRouterSchemaEnum.DUBBO.getCode().equals(routeConfig.getSchema())) {
        this.createDubboGenericInvoke(routeConfig);
      }
    }
    log.info("route info bind success");
  }

  private void initDubboInvoke() {
    dubboInvokeService.initConfig();
  }

  private void createDubboGenericInvoke(RouteConfig routeConfig) {
    String originUri = routeConfig.getUri();
    originUri = originUri.replaceAll(GatewayInfraConsoleSdkConstants.DUBBO_URL_PREFIX, "");
    String[] uriArray = originUri.split("#");
    dubboInvokeService.save(uriArray[0]);
  }
}