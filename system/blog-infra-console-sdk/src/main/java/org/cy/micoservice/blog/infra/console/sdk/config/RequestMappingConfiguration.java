package org.cy.micoservice.blog.infra.console.sdk.config;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.constants.gateway.GatewayInfraConsoleSdkConstants;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryListReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigSaveRequest;
import org.cy.micoservice.blog.infra.console.sdk.core.InfraConsoleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/11/27
 * @Description:
 */
@Slf4j
@Component
public class RequestMappingConfiguration implements CommandLineRunner {

  @Value("${spring.application.name:}")
  private String applicationName;

  @Value("${server.servlet.context-path:}")
  private String servletPath;

  @Autowired
  private RequestMappingHandlerMapping requestMappingHandlerMapping;

  @Autowired
  private InfraConsoleClient infraConsoleClient;

  @Override
  public void run(String... args) throws Exception {
    RouteConfigQueryListReq req = new RouteConfigQueryListReq();
    req.setUri(GatewayInfraConsoleSdkConstants.LB_SERVICE_PREFIX + applicationName);
    Set<RouteConfigSaveRequest> routeConfigs = infraConsoleClient.routeList(req);

    Set<String> invalidUrls = new HashSet<>();
    invalidUrls.add(GatewayInfraConsoleSdkConstants.API_ERROR_SIGN_PATH);

    Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
    Set<RouteConfigSaveRequest> routeConfigSaveRequestSet = handlerMethods.keySet().stream()
      .filter(requestMappingInfo -> {
        String requestPath = requestMappingInfo.getPathPatternsCondition().getPatternValues().stream().findAny().map(String::toString).orElse("");
        if (StringUtils.isBlank(requestPath)) {
          return false;
        }
        if (invalidUrls.contains(requestPath)) {
          return false;
        }

        String requestMethod = requestMappingInfo.getMethodsCondition().getMethods().stream().findFirst().map(Enum::name).orElse("");
        if (StringUtils.isBlank(requestMethod)) {
          return false;
        }
        return true;
      }).map(requestMappingInfo -> {
        String requestPath = requestMappingInfo.getPathPatternsCondition().getPatternValues().stream().findAny().map(String::toString).orElse("");
        String requestMethod = requestMappingInfo.getMethodsCondition().getMethods().stream().findFirst().map(Enum::name).orElse("");
        RouteConfigSaveRequest routeConfigSaveRequest = new RouteConfigSaveRequest();
        routeConfigSaveRequest.setSchema(GatewayInfraConsoleSdkConstants.HTTP_PROTOCOL);
        routeConfigSaveRequest.setMethod(requestMethod);
        routeConfigSaveRequest.setUri(GatewayInfraConsoleSdkConstants.LB_SERVICE_PREFIX + applicationName);
        routeConfigSaveRequest.setPath(servletPath + requestPath);
        routeConfigSaveRequest.setAuthType("jwt");
        return routeConfigSaveRequest;
      }).collect(Collectors.toSet());

    // 获取增量更新记录
    routeConfigSaveRequestSet.removeAll(routeConfigs);
    if (CollectionUtils.isEmpty(routeConfigSaveRequestSet)) {
      log.info("don't need update route config anymore");
      return;
    }

    // insert into DB
    for (RouteConfigSaveRequest request : routeConfigSaveRequestSet) {
      ApiResp<Long> routeConfig = infraConsoleClient.createRouteConfig(request);
      log.info("route config create response: {}", JSONObject.toJSONString(routeConfig));
    }
  }
}
