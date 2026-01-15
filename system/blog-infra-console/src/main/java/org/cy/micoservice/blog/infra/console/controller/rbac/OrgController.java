package org.cy.micoservice.blog.infra.console.controller.rbac;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.infra.facade.dto.org.OrgLevelDto;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.org.OrgListAllReq;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.org.OrgPageReq;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.org.OrgReq;
import org.cy.micoservice.blog.entity.infra.console.model.resp.sys.org.SysOrgResp;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.cy.micoservice.blog.infra.console.service.SysOrgService;
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
  @PostMapping("add")
  public ApiResp<String> add(@RequestBody @Validated({OrgReq.GroupAdd.class}) OrgReq req) {
    return orgService.add(req);
  }

  /**
   * edit org info
   * @param req
   * @return
   */
  @PostMapping("edit")
  public ApiResp<String> edit(@RequestBody @Validated({OrgReq.GroupEdit.class}) OrgReq req) {
    return orgService.edit(req);
  }

  /**
   * retrieve org info by tree struct
   * @return
   */
  @PostMapping("orgTreeList")
  public ApiResp<List<OrgLevelDto>> orgTreeList() {
    List<OrgLevelDto> orgLevelList = orgService.orgTree();
    return ApiResp.success(orgLevelList);
  }

  /**
   * retrieve page org list
   * @return
   */
  @PostMapping("/pageList")
  public ApiResp<PageResult<SysOrgResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) OrgPageReq req) {
    PageResult<SysOrgResp> list = orgService.pageList(req);
    return ApiResp.success(list);
  }

  @PostMapping("/list")
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
  @DeleteMapping("/delete")
  public ApiResp<String> delete(@RequestParam("surrogateId") @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return orgService.delete(surrogateId);
  }
}