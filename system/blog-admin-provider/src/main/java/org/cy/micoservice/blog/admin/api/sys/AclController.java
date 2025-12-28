package org.cy.micoservice.blog.admin.api.sys;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.admin.service.SysAclService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.req.acl.AclPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.acl.AclReq;
import org.cy.micoservice.blog.entity.admin.model.resp.acl.SysAclResp;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: acl api
 */
@Slf4j
@RestController
@RequestMapping("/sys/acl")
public class AclController {

  @Autowired
  private SysAclService aclService;

  /**
   * 分页查询权限点列表
   * @param req
   * @return
   * @throws Exception
   */
  @CheckAuth
  @RecordLogger
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
  @CheckAuth
  @RecordLogger
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Valid AclReq req) {
    return aclService.add(req);
  }

  /**
   * 修改权限点信息
   * @param req
   * @return
   */
  @CheckAuth
  @RecordLogger
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Valid AclReq req) {
    return aclService.edit(req);
  }

  /**
   * 删除权限点
   * @param aclId
   * @return
   */
  @RecordLogger
  @CheckAuth
  @DeleteMapping("/delete/{aclId}")
  public ApiResp<String> delete(@PathVariable("aclId") @NotNull(message = "surrogateId是必须的") Long aclId) {
    return aclService.delete(aclId);
  }

}

