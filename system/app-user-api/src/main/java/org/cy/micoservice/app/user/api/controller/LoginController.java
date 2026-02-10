package org.cy.micoservice.app.user.api.controller;

import org.cy.micoservice.app.common.base.api.ApiResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Lil-K
 * @Date: 2025/12/29
 * @Description: 用户登录
 */
@RestController
@RequestMapping("/login")
public class LoginController {

  @GetMapping("/check")
  public ApiResp<String> check() {
    return ApiResp.success();
  }
}