package org.cy.micoservice.blog.admin.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.admin.api.service.SysAclModuleService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysAclModuleFacade;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.blog.entity.admin.model.req.sys.aclmodule.AclModuleListReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.aclmodule.AclModuleReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.aclmodule.SysAclModuleResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: acl module
 */
@Service
@Slf4j
public class SysAclModuleServiceImpl implements SysAclModuleService {

  @DubboReference(check = false)
  private SysAclModuleFacade aclModuleFacade;

  @Override
  public ApiResp<String> add(AclModuleReq req) {
    return aclModuleFacade.add(req);
  }

  @Override
  public ApiResp<String> edit(AclModuleReq req) {
    return aclModuleFacade.edit(req);
  }

  @Override
  public ApiResp<List<AclModuleDto>> aclModuleTree() {
    return aclModuleFacade.aclModuleTree();
  }

  @Override
  public ApiResp<String> delete(Long surrogateId) {
    return aclModuleFacade.delete(surrogateId);
  }

  @Override
  public ApiResp<SysAclModuleResp> getAclModule(Long surrogateId) {
    return aclModuleFacade.getAclModule(surrogateId);
  }

  @Override
  public ApiResp<List<SysAclModuleResp>> list(AclModuleListReq req) {
    return aclModuleFacade.list(req);
  }
}
