package org.cy.micoservice.blog.admin.api.service;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.req.sys.role.RoleListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.role.RoleSaveReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.role.SysRoleResp;

/**
 * @Author: Lil-K
 * @Date: 2025/3/12
 * @Description:
 */
public interface SysRoleService {

	ApiResp<String> add(RoleSaveReq req);

	ApiResp<String> edit(RoleSaveReq req);

	ApiResp<String> freeze(RoleSaveReq req);

	ApiResp<String> delete(Long surrogateId);

	PageResult<SysRoleResp> pageList(RoleListPageReq req);

	boolean checkSupperAdminExist();
}
