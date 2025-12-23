package org.cy.micoservice.blog.gateway.service;


import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
public interface RouteConfigChangeLogService {

  /**
   * 根据指定 version 查询路由变更记录
   * @param version
   * @return
   */
  List<RouteChangeLog> findGtVersion(Long version);
}