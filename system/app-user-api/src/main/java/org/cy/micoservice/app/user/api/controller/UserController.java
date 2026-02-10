package org.cy.micoservice.app.user.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.entity.user.model.provider.pojo.User;
import org.cy.micoservice.app.framework.web.starter.web.RequestContext;
import org.cy.micoservice.app.user.api.service.UserEnterService;
import org.cy.micoservice.app.user.api.service.UserProfileService;
import org.cy.micoservice.app.user.api.vo.resp.SysUserResp;
import org.cy.micoservice.app.user.facade.provider.req.UserEnterInitReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
  @Autowired
  private UserEnterService userEnterService;

  @GetMapping("/profile")
  public ApiResp<User> profile() {
    User user = userProfileService.profile(RequestContext.getUserId());
    return ApiResp.success(user);
  }

  @GetMapping("/getUser")
  public ApiResp<SysUserResp> getUser(Long userId) {
    return userProfileService.getUserBySurrogateId(userId);
  }

  /**
   * 发送MQ通知, 用户进入程序时触发, 通知下游预加载聊天数据
   * 这个接口请求量非常大
   * @return
   */
  @PostMapping("/init")
  public ApiResp<Boolean> init(@RequestBody UserEnterInitReqDTO req) {
    req.setUserId(RequestContext.getUserId());
    return ApiResp.success(userEnterService.enter(req));
  }
}