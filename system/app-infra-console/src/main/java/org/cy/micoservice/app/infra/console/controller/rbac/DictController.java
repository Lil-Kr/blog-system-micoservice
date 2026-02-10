package org.cy.micoservice.app.infra.console.controller.rbac;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.dict.*;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.dic.SysDictDetailResp;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.dic.SysDictResp;
import org.cy.micoservice.app.entity.base.model.api.BasePageReq;
import org.cy.micoservice.app.infra.console.service.SysDictDetailService;
import org.cy.micoservice.app.infra.console.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: dict api
 */
@RestController
@RequestMapping("/sys/dict")
@Slf4j
public class DictController {

  @Autowired
  private SysDictService dictService;

  @Autowired
  private SysDictDetailService dictDetailService;

  @PostMapping("/add")
  public ApiResp<String> add (@RequestBody @Validated({DictSaveReq.DictAddGroup.class}) DictSaveReq req) {
    return dictService.add(req);
  }

  @PostMapping("/edit")
  public ApiResp<String> edit (@RequestBody @Validated({DictSaveReq.DictEditGroup.class}) DictSaveReq req) {
    return dictService.edit(req);
  }

  @DeleteMapping("/delete")
  public ApiResp<String> delete (@RequestParam("surrogateId") @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return dictService.delete(surrogateId);
  }

  /**
   * 数据字典列表
   * @return
   * @throws Exception
   */
  @PostMapping("/pageList")
  public ApiResp<PageResult<SysDictResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) DictListPageReq req) {
    PageResult<SysDictResp> res = dictService.pageList(req);
    return ApiResp.success(res);
  }

  /**
   * 获取字典明细树, mapping [key, SysDictDetailResp]
   * @return
   */
  @GetMapping("/dictDetailMapping")
  public ApiResp<Map<String, List<SysDictDetailResp>>> dictDetailMapping() {
    return dictService.dictDetailMapping();
  }

  /**
   * 字典明细分页查询
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/pageDictDetailList")
  public ApiResp<PageResult<SysDictDetailResp>> pageDictDetailList (@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) DictDetailPageListReq req) {
    PageResult<SysDictDetailResp> res = dictDetailService.pageDictDetailList(req);
    return ApiResp.success(res);
  }

  /**
   * 新增字典明细
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/addDetail")
  public ApiResp<String> addDetail (@RequestBody @Validated({SaveDictDetailReq.AddDictDetail.class}) SaveDictDetailReq req) {
    return dictDetailService.addDetail(req);
  }

  /**
   * 新增字典明细
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/editDetail")
  public ApiResp<String> editDetail (@RequestBody @Validated({SaveDictDetailReq.EditDictDetail.class}) SaveDictDetailReq req) {
    return dictDetailService.editDetail(req);
  }

  /**
   * 删除字典明细明细
   * @param surrogateId
   * @return
   * @throws Exception
   */
  @DeleteMapping("/deleteDetail")
  public ApiResp<String> deleteDetail (@RequestParam("surrogateId") @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return dictDetailService.deleteDetail(surrogateId);
  }

  /**
   * 不分页
   * 根据 字典主表id 查询相应的字典明细信息
   * @param req
   * @return
   */
  @PostMapping("/dictDetailList")
  public ApiResp<SysDictResp> dictDetailList(@RequestBody @Validated({DictDetailReq.GroupGetDictDetail.class}) DictDetailReq req) {
    return dictService.dictDetailList(req);
  }
}
