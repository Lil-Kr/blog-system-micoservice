package org.cy.micoservice.blog.admin.api.blog;

import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.admin.pojo.req.blog.category.BlogCategoryPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.category.BlogCategoryReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogCategoryResp;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogContentGroupResp;
import org.cy.micoservice.blog.admin.service.BlogCategoryService;
import org.cy.micoservice.blog.admin.service.BlogContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2024/4/6
 * @Description: blog type api
 */
@Slf4j
@RestController
@RequestMapping("/blog/category")
public class CategoryController {

  @Autowired
  private BlogCategoryService blogCategoryService;

  @Autowired
  private BlogContentService blogContentService;

  @RecordLogger
  @CheckAuth
  @PostMapping("/pageList")
  public ApiResp<PageResult<BlogCategoryResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) BlogCategoryPageReq req) {
    PageResult<BlogCategoryResp> list = blogCategoryService.pageList(req);
    return ApiResp.success(list);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/list")
  public ApiResp<PageResult<BlogCategoryResp>> list(@RequestBody @Validated BlogCategoryPageReq req) {
    PageResult<BlogCategoryResp> list = blogCategoryService.list(req);
    return ApiResp.success(list);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({BlogCategoryReq.GroupTypeAdd.class}) BlogCategoryReq req) {
    return blogCategoryService.add(req);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({BlogCategoryReq.GroupTypeEdit.class}) BlogCategoryReq req) {
    return blogCategoryService.edit(req);
  }

  @RecordLogger
  @CheckAuth
  @DeleteMapping("/delete")
  public ApiResp<String> delete(@RequestParam("surrogateId") @Valid @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return blogCategoryService.delete(surrogateId);
  }

//	@RecordLogger
//	@CheckAuth
//	@PostMapping("/deleteBatch")
//	public ApiResp<String> deleteBatch(@RequestBody @Validated({BlogCategoryReq.GroupTypeDelBatch.class}) BlogCategoryReq req) {
//		if (CollectionUtils.isEmpty(req.getSurrogateIds())) return ApiResp.failure("surrogateIds不能为空");
//		if (!checkSurrogateIds(req.getSurrogateIds())) return ApiResp.failure("surrogateIds不规范");
//		return blogCategoryService.deleteBatch(req);
//	}

  /** =============== 门户网站接口 ===============**/
  @RecordLogger
  @GetMapping("/frontCategoryCountList")
  public ApiResp<List<BlogContentGroupResp>> frontCategoryCountList() {
    List<BlogContentGroupResp> blogContentGroupList = blogContentService.frontContentByGroupCategory();

//        Map<Long, BlogCategoryVO> blogCategoryAllMapCache = CacheManager.getBlogCategoryAllMapCache();
//        blogContentGroupList.forEach(item -> {
//            item.setCategoryName(blogCategoryAllMapCache.getOrDefault(item.getCategoryId(), new BlogCategoryVO()).getName());
//        });
    return ApiResp.success(blogContentGroupList);
  }

}
