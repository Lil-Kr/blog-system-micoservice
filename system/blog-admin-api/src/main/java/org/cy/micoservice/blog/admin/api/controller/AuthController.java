package org.cy.micoservice.blog.admin.api.controller;

import org.cy.micoservice.blog.admin.api.service.SysPermissionService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.req.sys.permission.PermissionReq;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/3/16
 * @Description: auth for user-admin api
 */
@RestController
@RequestMapping("/sys/auth")
public class AuthController {

  @Autowired
  private SysPermissionService permissionService;

  /**
   * current user-admin has menu and button permission
   * @return
   */
  @CheckAuth
  @RecordLogger
  @GetMapping("/permission")
  public ApiResp<Map<String, Object>> permission() {
    PermissionReq req = new PermissionReq();
    req.setAdminId(RequestContext.getUserId());
    return permissionService.permission(req);
  }
}