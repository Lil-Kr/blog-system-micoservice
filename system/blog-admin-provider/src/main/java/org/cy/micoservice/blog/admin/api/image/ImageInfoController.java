package org.cy.micoservice.blog.admin.api.image;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageInfoPageListReq;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageInfoReq;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageUploadReq;
import org.cy.micoservice.blog.admin.pojo.resp.image.ImageInfoResp;
import org.cy.micoservice.blog.admin.pojo.resp.image.ImageUploadResp;
import org.cy.micoservice.blog.admin.service.ImageInfoService;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Author: Lil-K
 * @Date: 2024/5/29
 * @Description: 图片相关
 */
@Slf4j
@RestController
@RequestMapping("/image/info")
public class ImageInfoController {

  @Autowired
  private ImageInfoService imageInfoService;

  @RecordLogger
  @CheckAuth
  @PostMapping("/pageList")
  public ApiResp<PageResult<ImageInfoResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) ImageInfoPageListReq req) {
    PageResult<ImageInfoResp> imageInfoVOPageResult = imageInfoService.pageImageInfoList(req);
    return ApiResp.success(imageInfoVOPageResult);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/list")
  public ApiResp<PageResult<ImageInfoResp>> list(@RequestBody @Validated ImageInfoPageListReq req) {
    PageResult<ImageInfoResp> imageInfoVOPageResult = imageInfoService.imageInfoList(req);
    return ApiResp.success(imageInfoVOPageResult);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({ImageInfoReq.GroupImageInfoAdd.class}) ImageInfoReq req) {
    return imageInfoService.add(req);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated(ImageInfoReq.GroupImageInfoEdit.class) ImageInfoReq req) {
    return imageInfoService.edit(req);
  }

  @RecordLogger
  @CheckAuth
  @GetMapping("/get/{surrogateId}")
  public ApiResp<ImageInfoResp> get(@PathVariable("surrogateId") @Valid @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return imageInfoService.get(surrogateId);
  }

  @RecordLogger
  @CheckAuth
  @DeleteMapping("/delete/{imageId}")
  public ApiResp<String> delete(@PathVariable("imageId") @Valid @NotNull(message = "imageId是必须的") Long imageId) {
    return imageInfoService.delete(imageId);
  }

  /**
   * TODO: check image size(2M)
   * upload image
   * @param
   * @return
   * @throws IOException
   */
  @RecordLogger
  @CheckAuth
  @PostMapping("/upload")
  public ApiResp<ImageUploadResp> upload(@ModelAttribute ImageUploadReq req) throws Exception {
    return imageInfoService.imageUpload(req);
  }

}