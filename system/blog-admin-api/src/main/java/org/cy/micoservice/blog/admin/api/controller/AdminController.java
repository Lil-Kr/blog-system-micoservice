package org.cy.micoservice.blog.admin.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.cy.micoservice.blog.admin.api.service.MessageLangService;
import org.cy.micoservice.blog.admin.api.service.SysAdminService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.common.constants.CommonConstants;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.*;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.admin.SysAdminResp;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: admin api
 */
@RestController
@RequestMapping("/sys/admin")
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
  public ApiResp<SysAdmin> login(@RequestBody @Validated({AdminLoginAdminReq.AdminLogin.class}) AdminLoginAdminReq req) {
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
    return adminService.registerAdmin(req);
  }

  /**
   * admin-user logout
   * @return
   */
  @DeleteMapping("/logout")
  public ApiResp<Integer> logout() {
    RequestContext.remove();
    return ApiResp.success(msgLangService.getMessage(CommonConstants.LANG_ZH, "admin.logout.success"));
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
   * create admin-user
   * @param req
   * @return
   */
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({AdminSaveReq.GroupAddUser.class}) AdminSaveReq req) {
    return adminService.add(req);
  }

  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({AdminSaveReq.GroupEditUser.class}) AdminSaveReq req) {
    return adminService.edit(req);
  }

  @DeleteMapping("/delete/{adminId}")
  public ApiResp<String> delete(@PathVariable("adminId") @NotNull(message = "用户id是必须的") Long adminId) {
    return adminService.delete(adminId);
  }

  /**
   * get one admin-user
   * @return
   */
  @GetMapping("/get")
  public ApiResp<SysAdmin> get() {
    // SysAdmin currentUser = RequestHolder.getCurrentUser();
    // SysAdmin currentUser = new SysAdmin();
    return ApiResp.success();
  }

  /**
   * upload admin-user avatar
   * @return
   */
  @PostMapping("/avatar")
  public ApiResp<String> avatar(@ModelAttribute AvatarUploadReq req) throws Exception {
    req.setAdminId(RequestContext.getUserId());
    return adminService.uploadAvatar(req);
  }
}
