package org.cy.micoservice.blog.admin.api.blog;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogLabel;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelListReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogLabelResp;
import org.cy.micoservice.blog.admin.service.BlogLabelService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2024/3/30
 * @Description: blog label api
 */
@Slf4j
@RestController
@RequestMapping("/blog/label")
public class LabelController {

  @Autowired
  private BlogLabelService blogLabelService;

  @RecordLogger
  @CheckAuth
  @PostMapping("/pageList")
  public ApiResp<PageResult<BlogLabelResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) BlogLabelPageReq req) {
    PageResult<BlogLabelResp> list = blogLabelService.pageList(req);
    return ApiResp.success(list);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/list")
  public ApiResp<PageResult<BlogLabel>> list(@RequestBody @Valid BlogLabelListReq req) {
    PageResult<BlogLabel> list = blogLabelService.list(req);
    return ApiResp.success(list);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({BlogLabelReq.GroupLabelSave.class}) BlogLabelReq req) {
    return blogLabelService.add(req);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({BlogLabelReq.GroupLabelSave.class}) BlogLabelReq req) {
    return blogLabelService.edit(req);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/delete")
  public ApiResp<String> delete(@RequestBody @Validated({BlogLabelReq.GroupLabelDel.class}) BlogLabelReq req) {
    return blogLabelService.delete(req);
  }

//	@RecordLogger
//	@CheckAuth
//	@PostMapping("/deleteBatch")
//	public ApiResp<String> deleteBatch(@RequestBody @Validated({BlogLabelReq.GroupLabelDelBatch.class}) BlogLabelReq req) {
//		if (CollectionUtils.isEmpty(req.getSurrogateIds())) return ApiResp.failure("surrogateIds不能为空");
//		if (!checkSurrogateIds(req.getSurrogateIds())) return ApiResp.failure("surrogateIds不规范");
//		return blogLabelService.deleteBatch(req);
//	}

  /** =============== 门户网站接口 ===============**/
  @RecordLogger
  @GetMapping("/frontLabelList")
  public ApiResp<List<BlogLabel>> frontLabelList() {
    PageResult<BlogLabel> list = blogLabelService.list(new BlogLabelListReq());
    return ApiResp.success(list.getList());
  }

}