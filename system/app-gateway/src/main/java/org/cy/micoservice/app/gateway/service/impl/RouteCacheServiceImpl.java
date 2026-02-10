package org.cy.micoservice.app.gateway.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.app.common.constants.CommonFormatConstants;
import org.cy.micoservice.app.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.app.gateway.service.RouteCacheService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Lil-K
 * @Date: 2025/11/28
 * @Description:
 */
@Service
public class RouteCacheServiceImpl implements RouteCacheService {

  private final Map<String, RouteConfig> routeConfigMap = new ConcurrentHashMap<>();

  /**
   * /api/admin/{adminId} --> /api/admin/**
   * @param method
   * @param path
   * @return
   */
  @Override
  public RouteConfig get(String method, String path) {
    if (StringUtils.isBlank(method) || StringUtils.isBlank(path)) return null;
    String key = String.format(CommonFormatConstants.COMMENT_FORMAT_COLON_SPLIT, method, path);
    return routeConfigMap.getOrDefault(key, null);
  }

  @Override
  public boolean put(RouteConfig routeConfig) {
    if (Objects.isNull(routeConfig) || StringUtils.isBlank(routeConfig.getMethod()) || StringUtils.isBlank(routeConfig.getPath())) {
      return false;
    }
    String key = String.format(CommonFormatConstants.COMMENT_FORMAT_COLON_SPLIT, routeConfig.getMethod(), routeConfig.getPath());
    routeConfigMap.put(key, routeConfig);
    return true;
  }

  @Override
  public boolean remove(String method, String path) {
    if (StringUtils.isBlank(method) || StringUtils.isBlank(path)) {
      return false;
    }
    String key = String.format(CommonFormatConstants.COMMENT_FORMAT_COLON_SPLIT, method, path);
    return routeConfigMap.remove(key) != null;
  }
}
