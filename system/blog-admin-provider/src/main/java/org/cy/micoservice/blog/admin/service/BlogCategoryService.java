package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.admin.pojo.req.blog.category.BlogCategoryPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.category.BlogCategoryReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogCategoryResp;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2024/4/6
 * @Description:
 */
public interface BlogCategoryService {

  PageResult<BlogCategoryResp> pageList(BlogCategoryPageReq req);

  PageResult<BlogCategoryResp> list(BlogCategoryPageReq req);

  ApiResp<String> add(BlogCategoryReq req);

  ApiResp<String> edit(BlogCategoryReq req);

  ApiResp<String> delete(Long surrogateId);

  ApiResp<String> deleteBatch(BlogCategoryReq req);

  List<BlogCategoryResp> frontList();
}
