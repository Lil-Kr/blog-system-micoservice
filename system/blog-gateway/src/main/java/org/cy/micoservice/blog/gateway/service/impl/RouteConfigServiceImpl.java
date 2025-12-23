package org.cy.micoservice.blog.gateway.service.impl;

import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.gateway.dao.RouteConfigMapper;
import org.cy.micoservice.blog.entity.gateway.model.enums.GatewayRouterStatusEnum;
import org.cy.micoservice.blog.gateway.service.RouteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Service
public class RouteConfigServiceImpl implements RouteConfigService {

  @Autowired
  private RouteConfigMapper routeConfigMapper;

  @Override
  public List<RouteConfig> routeConfigAllValidaList() {
    return routeConfigMapper.routeConfigAllValidaList(GatewayRouterStatusEnum.VALID.getCode());
  }

  @Override
  public List<RouteConfig> findInConfigIds(Set<Long> saveConfigIds) {
    return routeConfigMapper.findInConfigIds(saveConfigIds);
  }
}