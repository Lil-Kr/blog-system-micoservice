package org.cy.micoservice.blog.infra.console.sdk.http;

import feign.Headers;
import feign.RequestLine;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryListReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigSaveReq;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/5
 * @Description: 统一控制台接口 facade 定义
 */
public interface InfraConsoleFacade {

  @RequestLine("POST /api/route/config/createInternal")
  @Headers("Content-Type: application/json")
  ApiResp<Long> createRouteConfig(RouteConfigSaveReq req);

  @RequestLine("POST /api/route/config/listInternal")
  @Headers("Content-Type: application/json")
  ApiResp<List<RouteConfig>> routeList(RouteConfigQueryListReq req);
}
