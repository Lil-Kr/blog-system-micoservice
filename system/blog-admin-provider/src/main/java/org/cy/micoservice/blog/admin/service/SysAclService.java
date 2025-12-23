package org.cy.micoservice.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.SysAcl;
import org.cy.micoservice.blog.entity.admin.model.req.acl.AclPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.acl.AclReq;
import org.cy.micoservice.blog.entity.admin.model.resp.acl.SysAclResp;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Lil-K
 * @Date: 2025/3/9
 * @Description:
 */
public interface SysAclService extends IService<SysAcl> {

  ApiResp<String> add(AclReq req);

  ApiResp<String> edit(AclReq req);

  PageResult<SysAclResp> pageList(AclPageReq req);

  ApiResp<ConcurrentHashMap<String, Object>> acls(AclReq req);

  ApiResp<String> delete(Long id);

  Long getAclCountByAclModuleId(Long aclModuleId);
}
