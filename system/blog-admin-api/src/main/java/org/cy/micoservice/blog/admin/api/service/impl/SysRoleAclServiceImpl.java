package org.cy.micoservice.blog.admin.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.admin.api.service.SysRoleAclService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysRoleAclFacade;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.req.sys.roleacl.RoleAclSaveReq;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
@Service
@Slf4j
public class SysRoleAclServiceImpl implements SysRoleAclService {

	@DubboReference(check = false)
	private SysRoleAclFacade roleAclFacade;

	@Override
	public ApiResp<String> updateRoleAcls(RoleAclSaveReq req) {
		return roleAclFacade.updateRoleAcls(req);
	}
}
