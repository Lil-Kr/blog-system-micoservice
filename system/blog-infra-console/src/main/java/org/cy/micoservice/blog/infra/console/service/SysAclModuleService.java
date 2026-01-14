package org.cy.micoservice.blog.infra.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAclModule;
import org.cy.micoservice.blog.entity.admin.model.req.sys.aclmodule.AclModuleListReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.aclmodule.AclModuleReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.aclmodule.SysAclModuleResp;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: acl module
 */
public interface SysAclModuleService extends IService<SysAclModule> {

  ApiResp<String> add(AclModuleReq req);

  ApiResp<String> edit(AclModuleReq req);

  ApiResp<List<AclModuleDto>> aclModuleTree();

  ApiResp<String> delete(Long surrogateId);

  ApiResp<SysAclModuleResp> getAclModule(Long surrogateId);

  ApiResp<List<SysAclModuleResp>> list(AclModuleListReq req);
}
