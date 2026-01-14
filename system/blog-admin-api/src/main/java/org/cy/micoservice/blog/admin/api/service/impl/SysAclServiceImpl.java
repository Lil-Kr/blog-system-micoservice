package org.cy.micoservice.blog.admin.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.admin.api.service.SysAclService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysAclFacade;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.req.sys.acl.AclPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.acl.AclReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.acl.SysAclResp;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
@Slf4j
@Service
public class SysAclServiceImpl implements SysAclService {

  @DubboReference(check = false)
  private SysAclFacade aclFacade;

  @Override
  public ApiResp<String> add(AclReq req) {
    return aclFacade.add(req);
  }

  @Override
  public ApiResp<String> edit(AclReq req) {
    return aclFacade.add(req);
  }

  @Override
  public PageResult<SysAclResp> pageList(AclPageReq req) {
    return aclFacade.pageList(req);
  }

  @Override
  public ApiResp<ConcurrentHashMap<String, Object>> acls(AclReq req) {
    return aclFacade.acls(req);
  }

  @Override
  public ApiResp<String> delete(Long id) {
    return aclFacade.delete(id);
  }

  @Override
  public Long getAclCountByAclModuleId(Long aclModuleId) {
    return aclFacade.getAclCountByAclModuleId(aclModuleId);
  }
}
