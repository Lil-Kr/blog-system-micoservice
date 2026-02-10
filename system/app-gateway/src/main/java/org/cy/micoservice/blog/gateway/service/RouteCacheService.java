package org.cy.micoservice.blog.gateway.service;

import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;

/**
 * @Author: Lil-K
 * @Date: 2025/11/28
 * @Description: 路由本地缓存service
 */
public interface RouteCacheService {

  /**
   * 获取路由配置
   * @param method
   * @param path
   * @return
   */
  RouteConfig get(String method, String path);

  /**
   * 添加路由配置
   * @param routeConfig
   */
  boolean put(RouteConfig routeConfig);

  /**
   * 删除路由配置
   * @param method
   * @param path
   */
  boolean remove(String method, String path);
}