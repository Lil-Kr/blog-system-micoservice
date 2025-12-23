package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.admin.pojo.resp.image.ImageCategoryResp;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageCategoryListReq;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageCategoryPageListReq;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageCategoryReq;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2024/5/29
 * @Description:
 */
public interface ImageCategoryService {

  PageResult<ImageCategoryResp> pageList(ImageCategoryPageListReq req);

  ApiResp<String> add(ImageCategoryReq req);

  ApiResp<String> edit(ImageCategoryReq req);

  PageResult<ImageCategoryResp> list(ImageCategoryListReq req);

  ApiResp<ImageCategoryResp> get(Long surrogateId);

  ApiResp<String> delete(Long surrogateId);

  List<ImageCategoryResp> nameList(ImageCategoryListReq req);
}
