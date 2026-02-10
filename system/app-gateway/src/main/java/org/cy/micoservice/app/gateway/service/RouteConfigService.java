package org.cy.micoservice.app.gateway.service;


import org.cy.micoservice.app.entity.gateway.model.entity.RouteConfig;
import java.util.List;
import java.util.Set;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
public interface RouteConfigService {

  List<RouteConfig> routeConfigAllValidaList();

  List<RouteConfig> findInConfigIds(Set<Long> saveConfigIds);

}