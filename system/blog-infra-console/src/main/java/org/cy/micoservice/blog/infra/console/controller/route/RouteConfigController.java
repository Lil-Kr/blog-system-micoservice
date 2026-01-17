package org.cy.micoservice.blog.infra.console.controller.route;

import jakarta.validation.Valid;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.entity.gateway.model.req.*;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.cy.micoservice.blog.infra.console.service.RouteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
@RestController
@RequestMapping("/route/config")
public class RouteConfigController {

  @Autowired
  private RouteConfigService routeConfigService;

  @PostMapping("/pageList")
  public ApiResp<PageResult<RouteConfig>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) RouteConfigQueryPageReq req) {
    PageResult<RouteConfig> routeConfigPageResult = routeConfigService.pageRouteConfigList(req);
    return ApiResp.success(routeConfigPageResult);
  }

  @PostMapping("/list")
  public ApiResp<List<RouteConfig>> list(@RequestBody @Valid RouteConfigQueryReq req) throws Exception {
    return routeConfigService.routeConfigList(req);
  }

  @PostMapping("/create")
  public ApiResp<Long> create(@RequestBody @Valid RouteConfigAddReq req) throws Exception {
    req.setAdminId(RequestContext.getUserId());
    return routeConfigService.create(req);
  }

  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Valid RouteConfigEditReq req) throws Exception {
    req.setAdminId(RequestContext.getUserId());
    return routeConfigService.edit(req);
  }

  @DeleteMapping("/delete")
  public ApiResp<String> delete(@Valid RouteConfigDelReq req) throws Exception {
    req.setAdminId(RequestContext.getUserId());
    return routeConfigService.delete(req);
  }

  @GetMapping("/appNameList")
  public ApiResp<List<RouteConfig>> getAppNameList() {
    return routeConfigService.getAppNameList();
  }

  @GetMapping("/getConfigLog")
  public ApiResp<List<RouteChangeLog>> getConfigLog(@Valid RouteConfigGetReq req) {
    return routeConfigService.getConfigLog(req.getConfigId());
  }
}