package org.cy.micoservice.app.infra.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAcl;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.acl.AclPageReq;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.acl.AclReq;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.acl.SysAclResp;

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
