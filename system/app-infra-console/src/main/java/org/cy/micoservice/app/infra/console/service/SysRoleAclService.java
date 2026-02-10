package org.cy.micoservice.app.infra.console.service;

import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.roleacl.RoleAclSaveReq;

/**
 * @Author: Lil-K
 * @Date: 2025/3/15
 * @Description: SysRoleAclService
 */
public interface SysRoleAclService {
	ApiResp<String> updateRoleAcls(RoleAclSaveReq param);
}
