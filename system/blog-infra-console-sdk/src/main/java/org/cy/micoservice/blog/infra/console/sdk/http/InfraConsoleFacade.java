package org.cy.micoservice.blog.infra.console.sdk.http;

import feign.Headers;
import feign.RequestLine;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryListReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigSaveRequest;

import java.util.List;

/**
 * @Author Lil-K
 * @Date: Created at 2025/10/5
 * @Description: 统一控制台接口 facade 定义
 */
public interface InfraConsoleFacade {

  @RequestLine("POST /api/route/config/create")
  @Headers("Content-Type: application/json")
  ApiResp<Long> createRouteConfig(RouteConfigSaveRequest req);

  @RequestLine("POST /api/route/config/list")
  @Headers("Content-Type: application/json")
  ApiResp<List<RouteConfig>> routeList(RouteConfigQueryListReq req);
}
