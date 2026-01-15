package org.cy.micoservice.blog.infra.console.config;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.constants.gateway.GatewayInfraConsoleSdkConstants;
import org.cy.micoservice.blog.common.enums.biz.AuthTypeEnum;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigAddReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigSaveReq;
import org.cy.micoservice.blog.infra.console.service.RouteConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/11/27
 * @Description: 加载所有的api接口
 * 注意: 这个配置每个api层都需要独立写, 不能写到这个sdk中, 这里为了方便测试
 */
@Slf4j
@Component
public class RequestMappingConfiguration implements CommandLineRunner {

  @Value("${spring.application.name:}")
  private String appName;

  @Value("${server.servlet.context-path:}")
  private String servletPath;

  @Autowired
  private RequestMappingHandlerMapping requestMappingHandlerMapping;

  @Autowired
  private RouteConfigService routeConfigService;

  @Override
  public void run(String... args) throws Exception {
    RouteConfigQueryReq req = new RouteConfigQueryReq();
    req.setUri(GatewayInfraConsoleSdkConstants.LB_SERVICE_PREFIX + appName);
    ApiResp<List<RouteConfig>> listApiResp = routeConfigService.routeConfigList(req);

    Set<RouteConfigSaveReq> routeConfigs = listApiResp.getData().stream().map(routeConfig -> {
      RouteConfigSaveReq request = new RouteConfigSaveReq();
      BeanUtils.copyProperties(routeConfig, request);
      return request;
    }).collect(Collectors.toSet());

    Set<String> invalidUrls = new HashSet<>();
    invalidUrls.add(GatewayInfraConsoleSdkConstants.API_ERROR_SIGN_PATH);

    Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
    Set<RouteConfigSaveReq> routeConfigSaveReqSet = handlerMethods.keySet().stream()
      .filter(requestMappingInfo -> {
        String requestPath = requestMappingInfo.getPathPatternsCondition().getPatternValues().stream().findAny().map(String::toString).orElse("");
        if (StringUtils.isBlank(requestPath) || invalidUrls.contains(requestPath)) {
          return false;
        }
        String requestMethod = requestMappingInfo.getMethodsCondition().getMethods().stream().findFirst().map(Enum::name).orElse("");
        if (StringUtils.isBlank(requestMethod)) return false;
        return true;
      }).map(requestMappingInfo -> {
        String requestPath = requestMappingInfo.getPathPatternsCondition().getPatternValues().stream().findAny().map(String::toString).orElse("");
        String requestMethod = requestMappingInfo.getMethodsCondition().getMethods().stream().findFirst().map(Enum::name).orElse("");
        RouteConfigSaveReq routeConfigSaveReq = new RouteConfigSaveReq();
        routeConfigSaveReq.setAppName(appName);
        routeConfigSaveReq.setSchema(GatewayInfraConsoleSdkConstants.HTTP_PROTOCOL);
        routeConfigSaveReq.setMethod(requestMethod);
        routeConfigSaveReq.setUri(GatewayInfraConsoleSdkConstants.LB_SERVICE_PREFIX + appName);
        routeConfigSaveReq.setPath(servletPath + requestPath);
        routeConfigSaveReq.setAuthType(AuthTypeEnum.JWT.getCode());
        return routeConfigSaveReq;
      }).collect(Collectors.toSet());

    // 获取增量更新记录
    routeConfigSaveReqSet.removeAll(routeConfigs);
    if (CollectionUtils.isEmpty(routeConfigSaveReqSet)) {
      log.info("don't need update route config anymore");
      return;
    }

    routeConfigSaveReqSet.stream()
      .map(routeConfigSaveReq -> {
        RouteConfigAddReq addReq = new RouteConfigAddReq();
        BeanUtils.copyProperties(routeConfigSaveReq, addReq);
        return addReq;
      })
    .forEach(routeConfigAddReq -> {
      try {
        routeConfigService.create(routeConfigAddReq);
      } catch (Exception e) {
        log.error("create route config failed, request: {}", JSONObject.toJSONString(routeConfigAddReq), e);
      }
    });
  }
}
