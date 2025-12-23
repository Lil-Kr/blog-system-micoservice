package org.cy.micoservice.blog.user.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.user.api.service.UserProfileService;
import org.cy.micoservice.blog.user.api.vo.resp.SysUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserProfileService userProfileService;

  @GetMapping("/profile")
  public String profile(Long userId) {
    return userProfileService.profile(userId);
  }

  @GetMapping("/getUser")
  public ApiResp<SysUserResp> getUser(Long userId) {
    return userProfileService.getUserBySurrogateId(userId);
  }
}