package org.cy.micoservice.blog.admin.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysRoleAdminFacade;
import org.cy.micoservice.blog.admin.provider.service.SysRoleAdminService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.req.sys.roleuser.RoleAdminReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.role.RoleAdminResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Service
@DubboService
public class SysRoleAdminFacadeImpl implements SysRoleAdminFacade {

  @Autowired
  private SysRoleAdminService sysRoleAdminService;

  /**
   * 更新[角色-用户]信息
   * @param req
   * @return
   */
  @Override
  public ApiResp<String> updateRoleAdmins(RoleAdminReq req) {
    return sysRoleAdminService.updateRoleAdmins(req);
  }

  /**
   * 角色用户[待选列表-已选列表]
   * @param req
   * @return
   */
  @Override
  public ApiResp<RoleAdminResp> roleAdminList(RoleAdminReq req) {
    return sysRoleAdminService.roleAdminList(req);
  }
}
