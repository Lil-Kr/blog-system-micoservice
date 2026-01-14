package org.cy.micoservice.blog.admin.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysRoleFacade;
import org.cy.micoservice.blog.admin.provider.service.SysRoleService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.req.sys.role.RoleListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.role.RoleSaveReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.role.SysRoleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Service
@DubboService
public class SysRoleFacadeImpl implements SysRoleFacade {

  @Autowired
  private SysRoleService roleService;

  @Override
  public ApiResp<String> add(RoleSaveReq req) {
    return roleService.add(req);
  }

  @Override
  public ApiResp<String> edit(RoleSaveReq req) {
    return roleService.edit(req);
  }

  @Override
  public ApiResp<String> freeze(RoleSaveReq req) {
    return roleService.freeze(req);
  }

  @Override
  public ApiResp<String> delete(Long surrogateId) {
    return roleService.delete(surrogateId);
  }

  @Override
  public PageResult<SysRoleResp> pageList(RoleListPageReq req) {
    return roleService.pageList(req);
  }

  @Override
  public boolean checkSupperAdminExist() {
    return roleService.checkSupperAdminExist();
  }
}
