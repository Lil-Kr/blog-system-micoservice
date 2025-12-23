package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.entity.admin.model.req.role.RoleListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.role.RoleSaveReq;
import org.cy.micoservice.blog.entity.admin.model.resp.role.SysRoleResp;

/**
 * @Author: Lil-K
 * @Date: 2025/3/12
 * @Description:
 */
public interface SysRoleService {

	ApiResp<String> add(RoleSaveReq param);

	ApiResp<String> edit(RoleSaveReq param);

	ApiResp<String> freeze(RoleSaveReq req);

	ApiResp<String> delete(Long surrogateId);

	PageResult<SysRoleResp> pageList(RoleListPageReq param);

	boolean checkSupperAdminExist();
}
