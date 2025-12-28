package org.cy.micoservice.blog.infra.console.service;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigAddReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigEditReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryPageReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryReq;
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

  ApiResp<Long> create(RouteConfigAddReq req) throws Exception;

  ApiResp<String> edit(RouteConfigEditReq req) throws Exception;

  ApiResp<String> delete(Long configId) throws Exception;

  ApiResp<List<RouteChangeLog>> getConfigLog(Long configId);
}