package org.cy.micoservice.blog.infra.console.controller.rbac;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.*;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.admin.SysAdminResp;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
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
  private SysAdminService userService;

  /**
   * admin login
   * @param req
   * @return
   */
  @PutMapping("/login")
  public ApiResp<SysAdmin> login(@RequestBody @Validated({AdminLoginReq.AdminLogin.class}) AdminLoginReq req) {
    return userService.adminLogin(req);
  }

  /**
   * admin register
   * @param req
   * @return
   */
  @PostMapping("/register")
  public ApiResp<Integer> register(@RequestBody @Valid AdminRegisterReq req) {
    return userService.registerAdmin(req);
  }

  /**
   * admin-user logout
   * @return
   */
  @DeleteMapping("/logout")
  public ApiResp<Integer> logout() {
    // remove user
    RequestContext.remove();
    return ApiResp.success(msgLangService.getMessage(LANG_ZH, "admin.logout.success"));
  }

  /**
   * 分页-查询用户列表
   * @param req
   * @return
   */
  @PostMapping("/pageList")
  public ApiResp<PageResult<SysAdminResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) AdminListPageReq req) {
    PageResult<SysAdminResp> result = userService.pageList(req);
    return ApiResp.success(result);
  }

  /**
   * create admin-user
   * @param req
   * @return
   */
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({AdminSaveReq.GroupAddUser.class}) AdminSaveReq req) {
    return userService.add(req);
  }

  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({AdminSaveReq.GroupEditUser.class}) AdminSaveReq req) {
    return userService.edit(req);
  }

  @DeleteMapping("/delete/{userId}")
  public ApiResp<String> delete(@PathVariable("userId") @NotNull(message = "用户id是必须的") Long userId) {
    return userService.delete(userId);
  }

  /**
   * get one admin-user
   * @return
   */
  @GetMapping("/get")
  public ApiResp<SysAdmin> get() {
    // SysAdmin currentUser = RequestHolder.getCurrentUser();
    return ApiResp.success(new SysAdmin());
  }

  /**
   * upload admin-user avatar
   * @return
   */
  @PostMapping("/avatar")
  public ApiResp<String> avatar(@ModelAttribute AvatarUploadReq req) throws Exception {
    return userService.uploadAvatar(req);
  }
}
