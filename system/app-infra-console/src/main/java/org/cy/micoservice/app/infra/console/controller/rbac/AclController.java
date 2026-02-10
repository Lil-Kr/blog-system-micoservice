package org.cy.micoservice.app.infra.console.controller.rbac;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.entity.base.model.api.BasePageReq;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.acl.AclDeleteReq;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.acl.AclPageReq;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.acl.AclReq;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.acl.SysAclResp;
import org.cy.micoservice.app.infra.console.service.SysAclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: acl api
 */
@RestController
@RequestMapping("/sys/acl")
@Slf4j
public class AclController {

  @Autowired
  private SysAclService aclService;

  /**
   * 分页查询权限点列表
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/pageList")
  public ApiResp<PageResult<SysAclResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) AclPageReq req) {
    PageResult<SysAclResp> res = aclService.pageList(req);
    return ApiResp.success(res);
  }

  /**
   * 权限点信息保存
   * @param req
   * @return
   */
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Valid AclReq req) {
    return aclService.add(req);
  }

  /**
   * 修改权限点信息
   * @param req
   * @return
   */
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Valid AclReq req) {
    return aclService.edit(req);
  }

  /**
   * 删除权限点
   * @param req
   * @return
   */
  @DeleteMapping("/delete")
  public ApiResp<String> delete(@Valid AclDeleteReq req) {
    return aclService.delete(req.getAclId());
  }

}

