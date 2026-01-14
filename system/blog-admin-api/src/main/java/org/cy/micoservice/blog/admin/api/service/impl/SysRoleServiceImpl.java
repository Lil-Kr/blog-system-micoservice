package org.cy.micoservice.blog.admin.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.admin.api.service.SysRoleService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysRoleFacade;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.req.sys.role.RoleListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.role.RoleSaveReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.role.SysRoleResp;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/3/4
 * @Description: role service
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

	@DubboReference(check = false)
	private SysRoleFacade roleFacade;

	@Override
	public ApiResp<String> add(RoleSaveReq req) {
		return roleFacade.add(req);
	}

	@Override
	public ApiResp<String> edit(RoleSaveReq req) {
		return roleFacade.edit(req);
	}

	@Override
	public ApiResp<String> freeze(RoleSaveReq req) {
		return roleFacade.freeze(req);
	}

	@Override
	public ApiResp<String> delete(Long surrogateId) {
		return roleFacade.delete(surrogateId);
	}

	@Override
	public PageResult<SysRoleResp> pageList(RoleListPageReq req) {
		return roleFacade.pageList(req);
	}

	@Override
	public boolean checkSupperAdminExist() {
		return roleFacade.checkSupperAdminExist();
	}
}
