package org.cy.micoservice.blog.admin.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysAclModuleFacade;
import org.cy.micoservice.blog.admin.provider.service.SysAclModuleService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.blog.entity.admin.model.req.sys.aclmodule.AclModuleListReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.aclmodule.AclModuleReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.aclmodule.SysAclModuleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Service
@DubboService
public class SysAclModuleFacadeImpl implements SysAclModuleFacade {

  @Autowired
  private SysAclModuleService aclModuleService;

  @Override
  public ApiResp<String> add(AclModuleReq req) {
    return aclModuleService.add(req);
  }

  @Override
  public ApiResp<String> edit(AclModuleReq req) {
    return aclModuleService.edit(req);
  }

  @Override
  public ApiResp<List<AclModuleDto>> aclModuleTree() {
    return aclModuleService.aclModuleTree();
  }

  @Override
  public ApiResp<String> delete(Long surrogateId) {
    return aclModuleService.delete(surrogateId);
  }

  @Override
  public ApiResp<SysAclModuleResp> getAclModule(Long surrogateId) {
    return aclModuleService.getAclModule(surrogateId);
  }

  @Override
  public ApiResp<List<SysAclModuleResp>> list(AclModuleListReq req) {
    return aclModuleService.list(req);
  }
}
