package org.cy.micoservice.blog.infra.console.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigAddReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigEditReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryPageReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigQueryReq;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
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

  @NoAuthCheck
  @PostMapping("/page")
  public ApiResp<PageResult<RouteConfig>> pageRouteConfigList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) RouteConfigQueryPageReq req) {
    PageResult<RouteConfig> routeConfigPageResult = routeConfigService.pageRouteConfigList(req);
    return ApiResp.success(routeConfigPageResult);
  }

  @NoAuthCheck
  @PostMapping("/list")
  public ApiResp<List<RouteConfig>> list(@RequestBody @Valid RouteConfigQueryReq req) throws Exception {
    return routeConfigService.routeConfigList(req);
  }

  @NoAuthCheck
  @PostMapping("/create")
  public ApiResp<Long> create(@RequestBody @Valid RouteConfigAddReq req) throws Exception {
    return routeConfigService.create(req);
  }

  @NoAuthCheck
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Valid RouteConfigEditReq req) throws Exception {
    return routeConfigService.edit(req);
  }

  @NoAuthCheck
  @DeleteMapping("/delete/{configId}")
  public ApiResp<String> delete(@PathVariable("configId") @NotNull(message = "configId 是必须的") Long configId) throws Exception {
    return routeConfigService.delete(configId);
  }

  @NoAuthCheck
  @GetMapping("/getConfigLog/{configId}")
  public ApiResp<List<RouteChangeLog>> getConfigLog(@PathVariable("configId") @NotNull(message = "configId 是必须的") Long configId) {
    return routeConfigService.getConfigLog(configId);
  }
}