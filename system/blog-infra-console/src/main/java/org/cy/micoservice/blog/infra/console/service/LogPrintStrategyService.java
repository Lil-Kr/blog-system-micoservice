package org.cy.micoservice.blog.infra.console.service;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.gateway.model.entity.LogPrintStrategy;
import org.cy.micoservice.blog.entity.gateway.model.req.LogPrintStrategyAddReq;
import org.cy.micoservice.blog.entity.gateway.model.req.LogPrintStrategyEditReq;
import org.cy.micoservice.blog.entity.gateway.model.req.LogPrintStrategyPageReq;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description: 打印日志的策略 service
 */
public interface LogPrintStrategyService {

  /**
   * 分页查询
   * @return
   */
  PageResult<LogPrintStrategy> pagePrintStrategyList(LogPrintStrategyPageReq req);

  /**
   * 添加路由日志打印策略
   */
  ApiResp<String> add(LogPrintStrategyAddReq req);

  /**
   * 编辑 路由日志打印策略
   * @param req
   * @return
   */
  ApiResp<String> edit(LogPrintStrategyEditReq req);

  /**
   * 删除 路由日志打印策略
   * @param id
   * @return
   */
  ApiResp<String> delete(Long id);
}