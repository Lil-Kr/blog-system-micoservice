package org.cy.micoservice.blog.admin.facade.interfaces;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.req.sys.roleacl.RoleAclSaveReq;

/**
 * @Author: Lil-K
 * @Date: 2025/3/15
 * @Description: SysRoleAclService
 */
public interface SysRoleAclFacade {
	ApiResp<String> updateRoleAcls(RoleAclSaveReq req);
}
