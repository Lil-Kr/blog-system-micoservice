package org.cy.micoservice.blog.infra.console.controller.rbac;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.infra.facade.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.role.RoleListPageReq;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.role.RoleSaveReq;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.roleacl.RoleAclSaveReq;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.roleuser.RoleAdminReq;
import org.cy.micoservice.blog.entity.infra.console.model.resp.sys.role.RoleAdminResp;
import org.cy.micoservice.blog.entity.infra.console.model.resp.sys.role.SysRoleResp;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.cy.micoservice.blog.infra.console.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.cy.micoservice.blog.common.constants.CommonConstants.LANG_ZH;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: role api
 */
@RestController
@RequestMapping("/sys/role")
@Slf4j
public class RoleController {

  @Autowired
  private SysRoleService roleService;

  @Autowired
  private SysTreeService treeService;

  @Autowired
  private SysRoleAdminService roleAdminService;

  @Autowired
  private SysRoleAclService roleAclService;

  @Autowired
  private MessageLangService msgLangService;

  /**
   * page role info list
   * @param req
   * @return
   */
  @PostMapping("/pageList")
  public ApiResp<PageResult<SysRoleResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) RoleListPageReq req) {
    PageResult<SysRoleResp> res = roleService.pageList(req);
    return ApiResp.success(res);
  }

  /**
   * add role info
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({RoleSaveReq.GroupAdd.class}) RoleSaveReq req) {
    return roleService.add(req);
  }

  /**
   * edit role info
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({RoleSaveReq.GroupEdite.class}) RoleSaveReq req) {
    return roleService.edit(req);
  }

  /**
   * freeze role info
   * @param req
   * @return
   */
  @PostMapping("/freeze")
  public ApiResp<String> freeze(@RequestBody @Validated({RoleSaveReq.GroupFreeze.class}) RoleSaveReq req) {
    return roleService.freeze(req);
  }

  /**
   * delete role info
   * @param surrogateId
   */
  @DeleteMapping("/delete")
  public ApiResp<String> delete (@RequestParam("surrogateId") @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return roleService.delete(surrogateId);
  }

  /**
   * retrieve current admin have [role-acl] tree
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/roleAclTree")
  public ApiResp<List<AclModuleDto>> roleAclTree(@RequestBody @Validated({RoleSaveReq.GroupTreeOrDel.class}) RoleSaveReq req) {
    req.setAdminId(RequestContext.getUserId());
    List<AclModuleDto> aclModuleDtoList = treeService.roleAclTree(req);
    if (CollectionUtils.isEmpty(aclModuleDtoList)) {
      return ApiResp.failure(msgLangService.getMessage(LANG_ZH, "sys.role.api.resp.msg1"));
    }
    return ApiResp.success(aclModuleDtoList);
  }

  /**
   * query[role-admin] list
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/roleAdminList")
  public ApiResp<RoleAdminResp> roleAdminList(@RequestBody @Validated({RoleAdminReq.GroupRoleUserPageList.class}) RoleAdminReq req) {
    return roleAdminService.roleAdminList(req);
  }

  /**
   * update[role-acl]
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/updateRoleAcls")
  public ApiResp<String> updateRoleAcls(@RequestBody @Validated({RoleAclSaveReq.GroupUpdateRoleAcls.class}) RoleAclSaveReq req) {
    req.setAdminId(RequestContext.getUserId());
    return roleAclService.updateRoleAcls(req);
  }

  /**
   * update[role-admin]
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/updateRoleAdmins")
  public ApiResp<String> updateRoleAdmins(@RequestBody @Validated({RoleAdminReq.GroupChangeRoleUsers.class}) RoleAdminReq req) {
    req.setAdminId(RequestContext.getUserId());
    return roleAdminService.updateRoleAdmins(req);
  }
}
