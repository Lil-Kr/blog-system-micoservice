package org.cy.micoservice.blog.admin.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.dto.sys.admin.AdminLoginAdminReqDTO;
import org.cy.micoservice.blog.admin.facade.interfaces.SysAdminFacade;
import org.cy.micoservice.blog.admin.provider.service.SysAdminService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.*;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.admin.SysAdminResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description: 系统管理员服务
 */
@Service
@DubboService
public class SysAdminFacadeImpl implements SysAdminFacade {

  @Autowired
  private SysAdminService adminService;

  @Override
  public ApiResp<SysAdmin> adminLogin(AdminLoginAdminReqDTO reqDTO) {
    return adminService.adminLogin(reqDTO);
  }

  @Override
  public ApiResp<Integer> registerAdmin(AdminRegisterReq req) {
    return adminService.registerAdmin(req);
  }

  @Override
  public SysAdmin getAdminById(Long id) {
    return adminService.getAdminById(id);
  }

  @Override
  public ApiResp<String> add(AdminSaveReq req) {
    return adminService.add(req);
  }

  @Override
  public PageResult<SysAdminResp> pageList(AdminListPageReq req) {
    return adminService.pageList(req);
  }

  @Override
  public ApiResp<String> edit(AdminSaveReq req) {
    return adminService.edit(req);
  }

  @Override
  public ApiResp<String> delete(Long id) {
    return adminService.delete(id);
  }

  @Override
  public ApiResp<String> uploadAvatar(AvatarUploadReq req) throws Exception {
    return adminService.uploadAvatar(req);
  }
}
