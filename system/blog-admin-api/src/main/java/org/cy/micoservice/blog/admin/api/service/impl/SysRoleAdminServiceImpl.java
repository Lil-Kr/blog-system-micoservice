package org.cy.micoservice.blog.admin.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.admin.api.service.SysRoleAdminService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysRoleAdminFacade;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.req.sys.roleuser.RoleAdminReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.role.RoleAdminResp;
import org.springframework.stereotype.Service;


/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: role-user relation service
 */
@Service
@Slf4j
public class SysRoleAdminServiceImpl implements SysRoleAdminService {

	@DubboReference(check = false)
	private SysRoleAdminFacade roleAdminFacade;

	@Override
	public ApiResp<String> updateRoleAdmin(RoleAdminReq req) {
		return roleAdminFacade.updateRoleAdmins(req);
	}

	@Override
	public ApiResp<RoleAdminResp> roleAdminList(RoleAdminReq req) {
		return roleAdminFacade.roleAdminList(req);
	}
}
