package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.req.roleacl.RoleAclSaveReq;

/**
 * @Author: Lil-K
 * @Date: 2025/3/15
 * @Description: SysRoleAclService
 */
public interface SysRoleAclService {
	ApiResp<String> updateRoleAcls(RoleAclSaveReq param);
}
