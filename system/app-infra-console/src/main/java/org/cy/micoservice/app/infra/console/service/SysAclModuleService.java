package org.cy.micoservice.app.infra.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.infra.facade.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAclModule;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.aclmodule.AclModuleListReq;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.aclmodule.AclModuleReq;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.aclmodule.SysAclModuleResp;

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
