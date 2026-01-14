package org.cy.micoservice.blog.user.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.user.model.provider.po.User;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.user.api.service.UserEnterService;
import org.cy.micoservice.blog.user.api.service.UserProfileService;
import org.cy.micoservice.blog.user.api.vo.resp.SysUserResp;
import org.cy.micoservice.blog.user.facade.provider.req.UserEnterInitReqDTO;
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

  @NoAuthCheck
  @GetMapping("/profile/{userId}")
  public ApiResp<User> profile(@PathVariable("userId") Long userId) {
    User user = userProfileService.profile(userId);
    return ApiResp.success(user);
  }

  @NoAuthCheck
  @GetMapping("/getUser")
  public ApiResp<SysUserResp> getUser(Long userId) {
    return userProfileService.getUserBySurrogateId(userId);
  }

  /**
   * 发送MQ通知, 用户进入程序时触发, 通知下游预加载聊天数据
   * 这个接口请求量非常大
   * @return
   */
  @NoAuthCheck
  @PostMapping("/init")
  public ApiResp<Boolean> init(@RequestBody UserEnterInitReqDTO reqDTO) {
    // todo: 测试id, 后续删除
    // reqDTO.setUserId(RequestContext.getUserId());

    return ApiResp.success(userEnterService.enter(reqDTO));
  }
}