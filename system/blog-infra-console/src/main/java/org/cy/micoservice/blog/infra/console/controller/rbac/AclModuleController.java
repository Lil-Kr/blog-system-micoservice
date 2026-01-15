package org.cy.micoservice.blog.infra.console.controller.rbac;


import jakarta.validation.Valid;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.aclmodule.AclModuleDelReq;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.aclmodule.AclModuleGetReq;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.aclmodule.AclModuleListReq;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.aclmodule.AclModuleReq;
import org.cy.micoservice.blog.entity.infra.console.model.resp.sys.aclmodule.SysAclModuleResp;
import org.cy.micoservice.blog.infra.console.service.SysAclModuleService;
import org.cy.micoservice.blog.infra.facade.dto.aclmodule.AclModuleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: acl-module api
 */
@RestController
@RequestMapping("/sys/aclModule")
public class AclModuleController {

  @Autowired
  private SysAclModuleService aclModuleService;

  /**
   * create acl module
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({AclModuleReq.GroupAdd.class}) AclModuleReq req) {
    return aclModuleService.add(req);
  }

  /**
   * edit acl module
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({AclModuleReq.GroupEdit.class}) AclModuleReq req) {
    return aclModuleService.edit(req);
  }

  /**
   * get one record acl module
   * @param req
   * @return
   */
  @GetMapping("/getAclModule")
  public ApiResp<SysAclModuleResp> getAclModule(@Valid AclModuleGetReq req) {
    return aclModuleService.getAclModule(req.getSurrogateId());
  }

  /**
   * retrieve acl module tree
   * @throws Exception
   */
  @PostMapping("/aclModuleTree")
  public ApiResp<List<AclModuleDto>> aclModuleTree() {
    return aclModuleService.aclModuleTree();
  }

  /**
   * delete acl module
   * @return
   * @throws Exception
   */
  @DeleteMapping("/delete")
  public ApiResp<String> delete(@Valid AclModuleDelReq req) {
    return aclModuleService.delete(req.getSurrogateId());
  }

  /**
   * query acl module all list
   * @return
   */
  @PostMapping("/list")
  public ApiResp<List<SysAclModuleResp>> list(@RequestBody AclModuleListReq req) {
    return aclModuleService.list(req);
  }

}

