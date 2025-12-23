package org.cy.micoservice.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.blog.entity.admin.model.entity.SysAclModule;
import org.cy.micoservice.blog.entity.admin.model.req.aclmodule.AclModuleListReq;
import org.cy.micoservice.blog.entity.admin.model.req.aclmodule.AclModuleReq;
import org.cy.micoservice.blog.entity.admin.model.resp.aclmodule.SysAclModuleResp;

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
