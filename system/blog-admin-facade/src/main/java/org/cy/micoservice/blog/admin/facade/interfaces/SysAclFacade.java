package org.cy.micoservice.blog.admin.facade.interfaces;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.req.sys.acl.AclPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.acl.AclReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.acl.SysAclResp;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Lil-K
 * @Date: 2025/3/9
 * @Description:
 */
public interface SysAclFacade {

  ApiResp<String> add(AclReq req);

  ApiResp<String> edit(AclReq req);

  PageResult<SysAclResp> pageList(AclPageReq req);

  ApiResp<ConcurrentHashMap<String, Object>> acls(AclReq req);

  ApiResp<String> delete(Long id);

  Long getAclCountByAclModuleId(Long aclModuleId);
}
