package org.cy.micoservice.blog.infra.console.controller.rbac;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.cy.micoservice.blog.entity.infra.console.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.admin.*;
import org.cy.micoservice.blog.entity.infra.console.model.resp.sys.admin.SysAdminResp;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.cy.micoservice.blog.infra.console.service.MessageLangService;
import org.cy.micoservice.blog.infra.console.service.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.cy.micoservice.blog.common.constants.CommonConstants.LANG_ZH;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: user api
 */
@RestController
@RequestMapping("/sys/admin")
@Slf4j
public class AdminController {

  @Autowired
  private MessageLangService msgLangService;

  @Autowired
  private SysAdminService adminService;

  /**
   * admin login
   * @param req
   * @return
   */
  @NoAuthCheck
  @PutMapping("/login")
  public ApiResp<SysAdmin> login(@RequestBody @Validated({AdminLoginReq.AdminLogin.class}) AdminLoginReq req) throws Exception {
    return adminService.adminLogin(req);
  }

  /**
   * admin register
   * @param req
   * @return
   */
  @NoAuthCheck
  @PostMapping("/register")
  public ApiResp<Integer> register(@RequestBody @Valid AdminRegisterReq req) {
    req.setAdminId(RequestContext.getUserId());
    return adminService.registerAdmin(req);
  }

  /**
   * admin-user logout
   * @return
   */
  @DeleteMapping("/logout")
  public ApiResp<Integer> logout() {
    return ApiResp.success(msgLangService.getMessage(LANG_ZH, "admin.logout.success"));
  }

  @NoAuthCheck
  @GetMapping("/getToken")
  public ApiResp<SysAdmin> getToken() throws Exception {
    return adminService.getToken();
  }

  /**
   * 分页-查询用户列表
   * @param req
   * @return
   */
  @PostMapping("/pageList")
  public ApiResp<PageResult<SysAdminResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) AdminListPageReq req) {
    PageResult<SysAdminResp> result = adminService.pageList(req);
    return ApiResp.success(result);
  }

  /**
   * create admin
   * @param req
   * @return
   */
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({AdminSaveReq.GroupAddAdmin.class}) AdminSaveReq req) {
    return adminService.add(req);
  }

  /**
   * edit role info
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({AdminSaveReq.GroupEditAdmin.class}) AdminSaveReq req) {
    return adminService.edit(req);
  }

  /**
   * delete role info
   */
  @DeleteMapping("/delete")
  public ApiResp<String> delete(@Valid AdminDeleteReq req) {
    return adminService.delete(req.getAdminId());
  }

  /**
   * get one admin-user
   * @return
   */
  @GetMapping("/get")
  public ApiResp<SysAdmin> get() {
    // SysAdmin currentUser = RequestHolder.getCurrentUser();
    return ApiResp.success(SysAdmin.builder().build());
  }

  /**
   * upload admin-user avatar
   * @return
   */
  @PostMapping("/avatar")
  public ApiResp<String> avatar(@ModelAttribute AvatarUploadReq req) throws Exception {
    return adminService.uploadAvatar(req);
  }
}
