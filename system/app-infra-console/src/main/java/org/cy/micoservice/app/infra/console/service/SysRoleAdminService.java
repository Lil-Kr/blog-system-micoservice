package org.cy.micoservice.app.infra.console.service;


import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.roleuser.RoleAdminReq;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.role.RoleAdminResp;

/**
 * role-user service
 * @Author: Lil-K
 * @Date: 2025/3/31
 * @Description:
 */
public interface SysRoleAdminService {

	ApiResp<String> updateRoleAdmins(RoleAdminReq req);

	ApiResp<RoleAdminResp> roleAdminList(RoleAdminReq req);
}
