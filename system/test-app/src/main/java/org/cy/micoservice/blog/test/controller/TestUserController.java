package org.cy.micoservice.blog.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.test.pojo.TestUser;
import org.cy.micoservice.blog.test.pojo.req.UserReq;
import org.cy.micoservice.blog.test.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Lil-K
 * @Date: 2026/1/25
 * @Description: TestUser Controller
 */
@Slf4j
@RestController
@RequestMapping("/test/user")
public class TestUserController {

  @Autowired
  private TestUserService testUserService;

  /**
   * 根据 ID 查询用户
   * @param req 用户ID
   * @return 用户信息
   */
  @NoAuthCheck
  @GetMapping("/get")
  public TestUser getUserById(UserReq req) {
    return testUserService.getUserById(req.getId());
  }

  /**
   * 新增用户
   * @param req 用户信息
   * @return 是否成功
   */
  @NoAuthCheck
  @PostMapping("/create")
  public Boolean addUser(@RequestBody UserReq req) {
    TestUser convert = BeanCopyUtils.convert(req, TestUser.class);
    return testUserService.addUser(convert);
  }

  /**
   * 更新用户
   * @param req 用户信息
   * @return 是否成功
   */
  @NoAuthCheck
  @PutMapping("/update")
  public Boolean updateUser(@RequestBody UserReq req) {
    TestUser convert = BeanCopyUtils.convert(req, TestUser.class);
    return testUserService.updateUser(convert);
  }

  /**
   * 根据 ID 删除用户
   * @param req 用户ID
   * @return 是否成功
   */
  @NoAuthCheck
  @DeleteMapping("/delete")
  public Boolean deleteUserById(UserReq req) {
    return testUserService.deleteUserById(req.getId());
  }
}