package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.SysUser;
import org.cy.micoservice.blog.entity.admin.model.req.user.*;
import org.cy.micoservice.blog.entity.admin.model.resp.admin.SysUserResp;

/**
 * @Author: Lil-K
 * @Date: 2025/3/7
 * @Description:
 */
public interface SysUserService {

	SysUser getUserById(Long id);

	SysUserResp getUserBySurrogateId(Long surrogateId);

	ApiResp<SysUser> adminLogin(UserLoginAdminReq reqParam);

	ApiResp<Integer> registerAdmin(UserRegisterReq req);

	ApiResp<String> add(UserSaveReq req);

	PageResult<SysUserResp> pageList(UserListPageReq req);

	ApiResp<String> edit(UserSaveReq req);

	ApiResp<String> delete(Long surrogateId);

  ApiResp<String> uploadAvatar(AvatarUploadReq req) throws Exception;
}
