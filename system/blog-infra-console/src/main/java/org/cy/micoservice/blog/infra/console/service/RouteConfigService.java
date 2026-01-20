package org.cy.micoservice.blog.infra.console.service;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;
import org.cy.micoservice.blog.entity.gateway.model.req.*;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
public interface RouteConfigService {

  PageResult<RouteConfig> pageRouteConfigList(RouteConfigQueryPageReq req);

  ApiResp<List<RouteConfig>> routeConfigList(RouteConfigQueryReq req);
  ApiResp<List<RouteConfig>> routeConfigInternalList(RouteConfigQueryReq req);

  ApiResp<Long> create(RouteConfigAddReq req) throws Exception;

  ApiResp<Long> createInternal(RouteConfigAddReq req) throws Exception;

  ApiResp<String> edit(RouteConfigEditReq req) throws Exception;

  ApiResp<String> delete(RouteConfigDelReq req) throws Exception;

  ApiResp<List<RouteChangeLog>> getConfigLog(Long configId);

  ApiResp<List<RouteConfig>> getAppNameList();
}