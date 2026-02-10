package org.cy.micoservice.app.infra.console.service;

import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.role.RoleListPageReq;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.role.RoleSaveReq;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.role.SysRoleResp;

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
