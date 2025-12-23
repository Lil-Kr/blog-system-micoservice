package org.cy.micoservice.blog.user.provider.service;

import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.user.provider.pojo.entity.SysUser;
import org.cy.micoservice.blog.user.provider.pojo.req.*;
import org.cy.micoservice.blog.user.provider.pojo.resp.SysUserResp;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
public interface UserService {

  String queryUserById(Long userId);

  SysUser getUserById(Long id);

  SysUserResp getUserBySurrogateId(Long surrogateId);

  ApiResp<SysUser> adminLogin(UserLoginAdminReq req);

  ApiResp<Integer> registerAdmin(UserRegisterReq req);

  ApiResp<String> add(UserSaveReq req);

  PageResult<SysUserResp> pageList(UserListPageReq req);

  ApiResp<String> edit(UserSaveReq req);

  ApiResp<String> delete(Long surrogateId);

  ApiResp<String> uploadAvatar(AvatarUploadReq req) throws Exception;
}
