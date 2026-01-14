package org.cy.micoservice.blog.admin.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysAclFacade;
import org.cy.micoservice.blog.admin.provider.service.SysAclService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.req.sys.acl.AclPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.acl.AclReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.acl.SysAclResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Service
@DubboService
public class SysAclFacadeImpl implements SysAclFacade {

  @Autowired
  private SysAclService aclService;

  @Override
  public ApiResp<String> add(AclReq req) {
    return aclService.add(req);
  }

  @Override
  public ApiResp<String> edit(AclReq req) {
    return aclService.edit(req);
  }

  @Override
  public PageResult<SysAclResp> pageList(AclPageReq req) {
    return aclService.pageList(req);
  }

  @Override
  public ApiResp<ConcurrentHashMap<String, Object>> acls(AclReq req) {
    return aclService.acls(req);
  }

  @Override
  public ApiResp<String> delete(Long id) {
    return aclService.delete(id);
  }

  @Override
  public Long getAclCountByAclModuleId(Long aclModuleId) {
    return aclService.getAclCountByAclModuleId(aclModuleId);
  }
}
