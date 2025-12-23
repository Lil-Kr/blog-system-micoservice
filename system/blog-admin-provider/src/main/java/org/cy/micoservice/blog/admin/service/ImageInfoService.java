package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.admin.pojo.resp.image.ImageInfoResp;
import org.cy.micoservice.blog.admin.pojo.resp.image.ImageUploadResp;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageInfoPageListReq;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageInfoReq;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageUploadReq;

import java.io.IOException;

/**
 * @Author: Lil-K
 * @Date: 2024/5/29
 * @Description:
 */
public interface ImageInfoService {

  PageResult<ImageInfoResp> pageImageInfoList(ImageInfoPageListReq req);

  PageResult<ImageInfoResp> imageInfoList(ImageInfoPageListReq req);

  ApiResp<String> add(ImageInfoReq req);

  ApiResp<ImageInfoResp> get(Long surrogateId);

  Long countByImageCategoryId(Long surrogateId);

  ApiResp<String> delete(Long surrogateId);

  ApiResp<ImageUploadResp> imageUpload(ImageUploadReq req) throws IOException;

  ApiResp<String> edit(ImageInfoReq req);
}
