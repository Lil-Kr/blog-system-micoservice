package org.cy.micoservice.blog.infra.console.controller.route;

import jakarta.validation.Valid;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.cy.micoservice.blog.entity.gateway.model.entity.LogPrintStrategy;
import org.cy.micoservice.blog.entity.gateway.model.req.LogPrintStrategyAddReq;
import org.cy.micoservice.blog.entity.gateway.model.req.LogPrintStrategyEditReq;
import org.cy.micoservice.blog.entity.gateway.model.req.LogPrintStrategyPageReq;
import org.cy.micoservice.blog.entity.gateway.model.req.RouteConfigLogDelReq;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.cy.micoservice.blog.infra.console.service.LogPrintStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description:
 */
@RestController
@RequestMapping("/route/log")
public class RouteLogController {

  @Autowired
  private LogPrintStrategyService logPrintStrategyService;

  @NoAuthCheck
  @PostMapping("/page")
  public ApiResp<PageResult<LogPrintStrategy>> pageRouteConfigList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) LogPrintStrategyPageReq req) {
    PageResult<LogPrintStrategy> logPrintStrategyPageResult = logPrintStrategyService.pagePrintStrategyList(req);
    return ApiResp.success(logPrintStrategyPageResult);
  }

  @NoAuthCheck
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Valid LogPrintStrategyAddReq req) {
    req.setAdminId(RequestContext.getUserId());
    return logPrintStrategyService.add(req);
  }

  @NoAuthCheck
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Valid LogPrintStrategyEditReq req) {
    req.setAdminId(RequestContext.getUserId());
    return logPrintStrategyService.edit(req);
  }

  @NoAuthCheck
  @DeleteMapping("/delete")
  public ApiResp<String> delete(@Valid RouteConfigLogDelReq req) {
    req.setAdminId(RequestContext.getUserId());
    return logPrintStrategyService.delete(req);
  }
}