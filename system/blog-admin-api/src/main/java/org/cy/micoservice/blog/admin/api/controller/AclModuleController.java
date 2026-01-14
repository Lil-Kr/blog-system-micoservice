package org.cy.micoservice.blog.admin.api.controller;


import jakarta.validation.constraints.NotNull;
import org.cy.micoservice.blog.admin.api.service.SysAclModuleService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.blog.entity.admin.model.req.sys.aclmodule.AclModuleListReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.aclmodule.AclModuleReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.aclmodule.SysAclModuleResp;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
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
  @CheckAuth
  @RecordLogger
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({AclModuleReq.GroupAdd.class}) AclModuleReq req) {
    req.setAdminId(RequestContext.getUserId());
    return aclModuleService.add(req);
  }

  /**
   * edit acl module
   * @param req
   * @return
   * @throws Exception
   */
  @CheckAuth
  @RecordLogger
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({AclModuleReq.GroupEdit.class}) AclModuleReq req) {
    req.setAdminId(RequestContext.getUserId());
    return aclModuleService.edit(req);
  }

  /**
   * get one record acl module
   * @param surrogateId
   * @return
   */
  @CheckAuth
  @RecordLogger
  @GetMapping("/getAclModule/{surrogateId}")
  public ApiResp<SysAclModuleResp> getAclModule(@PathVariable("surrogateId") @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return aclModuleService.getAclModule(surrogateId);
  }

  /**
   * retrieve acl module tree
   * @throws Exception
   */
  @CheckAuth
  @RecordLogger
  @PostMapping("aclModuleTree")
  public ApiResp<List<AclModuleDto>> aclModuleTree() {
    return aclModuleService.aclModuleTree();
  }

  /**
   * delete acl module
   * @return
   * @throws Exception
   */
  @CheckAuth
  @RecordLogger
  @DeleteMapping("/delete/{surrogateId}")
  public ApiResp<String> delete(@PathVariable("surrogateId") @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return aclModuleService.delete(surrogateId);
  }

  /**
   * query acl module all list
   * @return
   */
  @CheckAuth
  @RecordLogger
  @PostMapping("/list")
  public ApiResp<List<SysAclModuleResp>> list(@RequestBody AclModuleListReq req) {
    return aclModuleService.list(req);
  }

}

