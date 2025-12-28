package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.req.roleuser.RoleUserReq;
import org.cy.micoservice.blog.entity.admin.model.resp.role.RoleUserResp;

/**
 * role-user service
 * @Author: Lil-K
 * @Date: 2025/3/31
 * @Description:
 */
public interface SysRoleUserService {

	ApiResp<String> updateRoleUsers(RoleUserReq req);

	ApiResp<RoleUserResp> roleUserList(RoleUserReq req);
}
