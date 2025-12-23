package org.cy.micoservice.blog.admin.api.image;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageCategoryListReq;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageCategoryPageListReq;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageCategoryReq;
import org.cy.micoservice.blog.admin.pojo.resp.image.ImageCategoryResp;
import org.cy.micoservice.blog.admin.service.ImageCategoryService;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author: Lil-K
 * @Date: 2024/4/24
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/image/category")
public class ImageCategoryController {

  @Autowired
  private ImageCategoryService imageCategoryService;

  @RecordLogger
  @CheckAuth
  @PostMapping("/pageList")
  public ApiResp<PageResult<ImageCategoryResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) ImageCategoryPageListReq req) {
    PageResult<ImageCategoryResp> blogTopicVOPageResult = imageCategoryService.pageList(req);
    return ApiResp.success(blogTopicVOPageResult);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/list")
  public ApiResp<PageResult<ImageCategoryResp>> list(@RequestBody @Valid ImageCategoryListReq req) {
    PageResult<ImageCategoryResp> blogTopicVOPageResult = imageCategoryService.list(req);
    return ApiResp.success(blogTopicVOPageResult);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/nameList")
  public ApiResp<List<ImageCategoryResp>> nameList(@RequestBody @Valid ImageCategoryListReq req) {
    return ApiResp.success(imageCategoryService.nameList(req));
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({ImageCategoryReq.GroupImageCategoryAdd.class}) ImageCategoryReq req) {
    return imageCategoryService.add(req);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({ImageCategoryReq.GroupImageCategoryEdit.class}) ImageCategoryReq req) {
    return imageCategoryService.edit(req);
  }

  @RecordLogger
  @CheckAuth
  @GetMapping("/get")
  public ApiResp<ImageCategoryResp> get(@RequestParam("surrogateId") @Valid @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return imageCategoryService.get(surrogateId);
  }

  @RecordLogger
  @CheckAuth
  @DeleteMapping("/delete")
  public ApiResp<String> delete(@RequestParam("surrogateId") @Valid @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return imageCategoryService.delete(surrogateId);
  }

}
