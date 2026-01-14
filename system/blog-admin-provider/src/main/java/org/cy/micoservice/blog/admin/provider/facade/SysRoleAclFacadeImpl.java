package org.cy.micoservice.blog.admin.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysRoleAclFacade;
import org.cy.micoservice.blog.admin.provider.service.SysRoleAclService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.req.sys.roleacl.RoleAclSaveReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Service
@DubboService
public class SysRoleAclFacadeImpl implements SysRoleAclFacade {

  @Autowired
  private SysRoleAclService roleAclService;

  @Override
  public ApiResp<String> updateRoleAcls(RoleAclSaveReq req) {
    return roleAclService.updateRoleAcls(req);
  }
}
