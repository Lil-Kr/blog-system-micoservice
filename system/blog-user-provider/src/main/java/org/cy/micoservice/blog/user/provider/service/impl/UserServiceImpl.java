package org.cy.micoservice.blog.user.provider.service.impl;

import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.user.provider.config.UserRedisKeyBuilder;
import org.cy.micoservice.blog.user.provider.dao.SysUserMapper;
import org.cy.micoservice.blog.user.provider.pojo.entity.SysUser;
import org.cy.micoservice.blog.user.provider.pojo.req.*;
import org.cy.micoservice.blog.user.provider.pojo.resp.SysUserResp;
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
  private SysUserMapper userMapper;

  @Autowired
  private UserRedisKeyBuilder userRedisKeyBuilder;

  @Override
  public String queryUserById(Long userId) {
    return "dubbo reques user info";
  }

  @Override
  public SysUser getUserById(Long id) {
    return userMapper.getUserById(id);
  }

  @Override
  public SysUserResp getUserBySurrogateId(Long surrogateId) {
    return userMapper.getUserBySurrogateId(surrogateId);
  }

  @Override
  public ApiResp<SysUser> adminLogin(UserLoginAdminReq req) {
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
  public PageResult<SysUserResp> pageList(UserListPageReq req) {
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

  @Override
  public ApiResp<String> uploadAvatar(AvatarUploadReq req) throws Exception {
    return null;
  }
}
