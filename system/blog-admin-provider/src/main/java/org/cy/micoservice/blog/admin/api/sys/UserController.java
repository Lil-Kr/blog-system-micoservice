package org.cy.micoservice.blog.admin.api.sys;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.admin.common.constants.CommonConstants;
import org.cy.micoservice.blog.admin.common.holder.RequestHolder;
import org.cy.micoservice.blog.admin.service.MessageLangService;
import org.cy.micoservice.blog.admin.service.SysUserService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.SysUser;
import org.cy.micoservice.blog.entity.admin.model.req.user.*;
import org.cy.micoservice.blog.entity.admin.model.resp.admin.SysUserResp;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: user api
 */
@RestController
@RequestMapping("/sys/user")
@Slf4j
public class UserController {

  @Autowired
  private MessageLangService msgLangService;

  @Autowired
  private SysUserService userService;

  /**
   * admin login
   * @param req
   * @return
   */
  @RecordLogger
  @PutMapping("/login")
  public ApiResp<SysUser> login(@RequestBody @Validated({UserLoginAdminReq.AdminLogin.class}) UserLoginAdminReq req) {
    return userService.adminLogin(req);
  }

  /**
   * admin register
   * @param req
   * @return
   */
  @RecordLogger
  @PostMapping("/register")
  public ApiResp<Integer> register(@RequestBody @Valid UserRegisterReq req) {
    return userService.registerAdmin(req);
  }

  /**
   * admin-user logout
   * @return
   */
  @CheckAuth
  @RecordLogger
  @DeleteMapping("/logout")
  public ApiResp<Integer> logout() {
    // remove user
    RequestHolder.remove();
    return ApiResp.success(msgLangService.getMessage(CommonConstants.LANG_ZH, "admin.logout.success"));
  }

  /**
   * 分页-查询用户列表
   * @param req
   * @return
   */
  @CheckAuth
  @RecordLogger
  @PostMapping("/pageList")
  public ApiResp<PageResult<SysUserResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) UserListPageReq req) {
    PageResult<SysUserResp> result = userService.pageList(req);
    return ApiResp.success(result);
  }

  /**
   * create admin-user
   * @param req
   * @return
   */
  @CheckAuth
  @RecordLogger
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({UserSaveReq.GroupAddUser.class}) UserSaveReq req) {
    return userService.add(req);
  }

  @CheckAuth
  @RecordLogger
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({UserSaveReq.GroupEditUser.class}) UserSaveReq req) {
    return userService.edit(req);
  }

  @CheckAuth
  @RecordLogger
  @DeleteMapping("/delete/{userId}")
  public ApiResp<String> delete(@PathVariable("userId") @NotNull(message = "用户id是必须的") Long userId) {
    return userService.delete(userId);
  }

  /**
   * get one admin-user
   * @return
   */
  @CheckAuth
  @RecordLogger
  @GetMapping("/get")
  public ApiResp<SysUser> get() {
    SysUser currentUser = RequestHolder.getCurrentUser();
    return ApiResp.success(currentUser);
  }

  /**
   * upload admin-user avatar
   * @return
   */
  @CheckAuth
  @RecordLogger
  @PostMapping("/avatar")
  public ApiResp<String> avatar(@ModelAttribute AvatarUploadReq req) throws Exception {
    return userService.uploadAvatar(req);
  }
}
