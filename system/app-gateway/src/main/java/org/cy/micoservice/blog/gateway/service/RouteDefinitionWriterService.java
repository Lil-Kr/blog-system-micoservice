package org.cy.micoservice.blog.gateway.service;


import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description: 路由定义配置service
 */
public interface RouteDefinitionWriterService {

  /**
   * 保存网关路由配置
   * @param routeConfig
   */
  boolean save(RouteConfig routeConfig);

  /**
   * 删除网关路由配置
   * @param configId
   */
  void delete(Long configId);
}