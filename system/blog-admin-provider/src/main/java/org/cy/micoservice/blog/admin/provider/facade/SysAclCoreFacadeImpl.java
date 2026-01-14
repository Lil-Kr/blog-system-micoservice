package org.cy.micoservice.blog.admin.provider.facade;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysAclCoreFacade;
import org.cy.micoservice.blog.admin.provider.service.SysAclCoreService;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAcl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Slf4j
@Service
@DubboService
public class SysAclCoreFacadeImpl implements SysAclCoreFacade {

  @Autowired
  private SysAclCoreService coreService;

  @Override
  public List<SysAcl> getCurrentAdminAclList() {
    return coreService.getCurrentAdminAclList();
  }

  @Override
  public List<SysAcl> getRoleAclList(Long roleId) {
    return coreService.getRoleAclList(roleId);
  }

  @Override
  public List<SysAcl> getAdminAclList(Long adminId) {
    return coreService.getAdminAclList(adminId);
  }

  @Override
  public List<SysAcl> getAdminAclList(Long adminId, Integer type) {
    return coreService.getAdminAclList(adminId, type);
  }

  @Override
  public boolean hasUrlAcl(String url) {
    return coreService.hasUrlAcl(url);
  }
}
