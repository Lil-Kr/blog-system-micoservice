package org.cy.micoservice.blog.admin.api.sys;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.admin.service.SysOrgService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.dto.org.OrgLevelDto;
import org.cy.micoservice.blog.entity.admin.model.req.org.OrgListAllReq;
import org.cy.micoservice.blog.entity.admin.model.req.org.OrgPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.org.OrgReq;
import org.cy.micoservice.blog.entity.admin.model.resp.org.SysOrgResp;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/3
 * @Description: org api
 */
@RestController
@RequestMapping("/sys/org")
@Slf4j
public class OrgController {

  @Autowired
  private SysOrgService orgService;

  /**
   * add org info
   * @param req
   * @return
   * @throws Exception
   */
  @RecordLogger
  @CheckAuth
  @PostMapping("add")
  public ApiResp<String> add(@RequestBody @Validated({OrgReq.GroupAdd.class}) OrgReq req) {
    return orgService.add(req);
  }

  /**
   * edit org info
   * @param req
   * @return
   */
  @RecordLogger
  @CheckAuth
  @PostMapping("edit")
  public ApiResp<String> edit(@RequestBody @Validated({OrgReq.GroupEdit.class}) OrgReq req) {
    return orgService.edit(req);
  }

  /**
   * retrieve org info by tree struct
   * @return
   */
  @RecordLogger
  @CheckAuth
  @PostMapping("orgTreeList")
  public ApiResp<List<OrgLevelDto>> orgTreeList() {
    List<OrgLevelDto> orgLevelList = orgService.orgTree();
    return ApiResp.success(orgLevelList);
  }

  /**
   * retrieve page org list
   * @return
   */
  @RecordLogger
  @CheckAuth
  @PostMapping("/pageList")
  public ApiResp<PageResult<SysOrgResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) OrgPageReq req) {
    PageResult<SysOrgResp> list = orgService.pageList(req);
    return ApiResp.success(list);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("list")
  public ApiResp<List<SysOrgResp>> list(@RequestBody OrgListAllReq req) {
    List<SysOrgResp> list = orgService.list(req);
    return ApiResp.success(list);
  }

  /**
   * delete org
   * @param surrogateId
   * @return
   * @throws Exception
   */
  @RecordLogger
  @CheckAuth
  @DeleteMapping("delete")
  public ApiResp<String> delete(@RequestParam("surrogateId") @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return orgService.delete(surrogateId);
  }
}