package org.cy.micoservice.blog.infra.console.sdk.core;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryListReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigSaveRequest;
import org.cy.micoservice.blog.infra.console.sdk.config.FeignClientFactory;
import org.cy.micoservice.blog.infra.console.sdk.config.NacosServiceDiscovery;
import org.cy.micoservice.blog.infra.console.sdk.config.SdkProperties;
import org.cy.micoservice.blog.infra.console.sdk.http.InfraConsoleFacade;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Lil-K
 * @Date: Created at 2025/10/5
 * @Description: 基础控制台client
 */
@Slf4j
public class InfraConsoleClient {

  private SdkProperties sdkProperties;
  private NacosServiceDiscovery nacosServiceDiscovery;
  private InfraConsoleFacade infraConsoleFacade;

  public InfraConsoleClient(SdkProperties sdkProperties) {
    this.sdkProperties = sdkProperties;
  }

  public void init() throws Exception {
    log.info("sdkProperties:{}", sdkProperties);
    // 1. 初始化 Nacos 服务发现
    nacosServiceDiscovery = new NacosServiceDiscovery(sdkProperties.getNacosAddress(),
      sdkProperties.getNacosNamespace(),
      sdkProperties.getNacosUser(),
      sdkProperties.getNacosPwd());

    // 2. 创建 Feign 客户端工厂
    FeignClientFactory factory = new FeignClientFactory(nacosServiceDiscovery);

    // 3. 创建 UserServiceClient (服务名: provider-service)
    infraConsoleFacade = factory.createClient(InfraConsoleFacade.class, sdkProperties.getInfraConsoleServiceName(), sdkProperties.getInfraConsoleServiceGroup(), sdkProperties.getClientName());
    log.info("InfraConsoleClient init success");
  }

  /**
   * 创建路由配置
   * @param request
   * @return
   */
  public ApiResp<Long> createRouteConfig(RouteConfigSaveRequest request) {
    return this.getInfraConsoleFacade().createRouteConfig(request);
  }

  public Set<RouteConfigSaveRequest> routeList(RouteConfigQueryListReq req) {
    ApiResp<List<RouteConfig>> resp = this.getInfraConsoleFacade().routeList(req);
    return resp.getData().stream().map(routeConfig -> {
      RouteConfigSaveRequest request = new RouteConfigSaveRequest();
      BeanUtils.copyProperties(routeConfig, request);
      return request;
    }).collect(Collectors.toSet());
  }

  private InfraConsoleFacade getInfraConsoleFacade() {
    return infraConsoleFacade;
  }
}
