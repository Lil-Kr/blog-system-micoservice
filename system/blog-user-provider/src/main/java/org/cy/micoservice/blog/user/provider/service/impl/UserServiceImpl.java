package org.cy.micoservice.blog.user.provider.service.impl;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.user.model.provider.po.User;
import org.cy.micoservice.blog.entity.user.model.provider.req.*;
import org.cy.micoservice.blog.entity.user.model.provider.resp.UserResp;
import org.cy.micoservice.blog.user.provider.config.UserRedisKeyBuilder;
import org.cy.micoservice.blog.user.provider.dao.UserMapper;
import org.cy.micoservice.blog.user.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserRedisKeyBuilder userRedisKeyBuilder;

  @Override
  public String queryUserById(Long userId) {
    return "dubbo reques user info";
  }

  @Override
  public User getUserById(Long userId) {
    return userMapper.getUserById(userId);
  }

  @Override
  public UserResp getUserBySurrogateId(Long surrogateId) {
    return userMapper.getUserBySurrogateId(surrogateId);
  }

  @Override
  public ApiResp<User> adminLogin(UserLoginAdminReq req) {
    return null;
  }

  @Override
  public ApiResp<Integer> registerAdmin(UserRegisterReq req) {
    return null;
  }

  @Override
  public ApiResp<String> add(UserSaveReq req) {
    return null;
  }

  @Override
  public PageResult<UserResp> pageList(UserListPageReq req) {
    return null;
  }

  @Override
  public ApiResp<String> edit(UserSaveReq req) {
    return null;
  }

  @Override
  public ApiResp<String> delete(Long surrogateId) {
    return null;
  }

}
