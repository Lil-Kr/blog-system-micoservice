package org.cy.micoservice.blog.gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.constants.gateway.GatewayInfraConsoleSdkConstants;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.gateway.config.async.GatewayAsyncTaskSubmitter;
import org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterSchemaEnum;
import org.cy.micoservice.blog.gateway.service.DubboInvokeService;
import org.cy.micoservice.blog.gateway.service.RouteCacheService;
import org.cy.micoservice.blog.gateway.service.RouteConfigService;
import org.cy.micoservice.blog.gateway.service.RouteDefinitionWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
  @Autowired
  private GatewayAsyncTaskSubmitter taskSubmitter;

  /**
   * 网关服务启动时执行该方法
   */
  @PostConstruct
  public void initRouteDefinition() throws ExecutionException, InterruptedException {
    log.info("first loading route config from db start");
    // 异步读取DB中的route config 数据
    CompletableFuture<List<RouteConfig>> routeConfigListFuture =
      taskSubmitter.supplyAsync("query-route-config-data",
        () -> routerConfigService.routeConfigAllValidaList(),
        Collections::emptyList,
        200);

    // 异步执行dubbo初始化配置
    CompletableFuture<Void> initDubboInvoke =
      taskSubmitter.runAsync("init-dubbo-nacos-config", this::initDubboInvoke, () -> {}, 50);
    CompletableFuture.allOf(routeConfigListFuture, initDubboInvoke).join();

    List<RouteConfig> routeConfigList = routeConfigListFuture.get();
    for (RouteConfig routeConfig : routeConfigList) {
      if (GatewayRouterSchemaEnum.HTTP.getCode().equals(routeConfig.getSchema())) {
        routeDefinitionWriterService.save(routeConfig);
        routeCacheService.put(routeConfig);
      } else {
        // init dubbo router <uri, dubboInvokeService>
        this.createDubboGenericInvoke(routeConfig);
      }
    }
    log.info("route info bind success");
  }

  /**
   * 初始化dubbo
   */
  private void initDubboInvoke() {
    dubboInvokeService.initConfig();
  }

  /**
   * 在缓存中添加转发dubbo请求数据
   * @param routeConfig
   */
  private void createDubboGenericInvoke(RouteConfig routeConfig) {
    String originUri = routeConfig.getUri();
    originUri = originUri.replaceAll(GatewayInfraConsoleSdkConstants.DUBBO_URL_PREFIX, "");
    // org.cy.micoservice.blog.user.facade.interfaces.UserFacade#test
    String[] uriArray = originUri.split("#");
    dubboInvokeService.save(uriArray[0]);
  }
}