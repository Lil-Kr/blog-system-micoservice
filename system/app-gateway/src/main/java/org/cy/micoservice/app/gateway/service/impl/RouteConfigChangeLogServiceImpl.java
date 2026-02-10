package org.cy.micoservice.app.gateway.service.impl;

import org.cy.micoservice.app.entity.gateway.model.entity.RouteChangeLog;
import org.cy.micoservice.app.gateway.dao.RouteChangeLogMapper;
import org.cy.micoservice.app.gateway.service.RouteConfigChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Service
public class RouteConfigChangeLogServiceImpl implements RouteConfigChangeLogService {

  @Autowired
  private RouteChangeLogMapper routeChangeLogMapper;

  @Override
  public List<RouteChangeLog> findGtVersion(Long version) {
    return routeChangeLogMapper.findGtVersion(version);
  }
}