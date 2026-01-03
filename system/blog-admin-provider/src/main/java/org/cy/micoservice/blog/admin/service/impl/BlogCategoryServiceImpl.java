package org.cy.micoservice.blog.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.admin.common.holder.RequestHolder;
import org.cy.micoservice.blog.admin.dao.BlogCategoryMapper;
import org.cy.micoservice.blog.admin.pojo.dto.blog.BlogCategoryDTO;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogCategory;
import org.cy.micoservice.blog.admin.pojo.req.blog.category.BlogCategoryPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.category.BlogCategoryReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogCategoryResp;
import org.cy.micoservice.blog.admin.service.BlogCategoryService;
import org.cy.micoservice.blog.admin.service.CacheService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.cy.micoservice.blog.admin.common.constants.CommonConstants.*;
import static org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum.*;

/**
 * @Author: Lil-K
 * @Date: 2024/4/6
 * @Description:
 */
@Slf4j
@Service
public class BlogCategoryServiceImpl implements BlogCategoryService {

  @Autowired
  private BlogCategoryMapper blogCategoryMapper;

  @Autowired
  private CacheService cacheService;

  @Override
  public PageResult<BlogCategoryResp> pageList(BlogCategoryPageReq req) {
    List<BlogCategoryResp> pageList = blogCategoryMapper.pageCategoryList(req);
    Integer count = blogCategoryMapper.getCountByList(req);
    if (CollectionUtils.isEmpty(pageList)) {
        return PageResult.emptyPage();
    }else {
        return new PageResult<>(pageList, count);
    }
  }

  @Override
  public PageResult<BlogCategoryResp> list(BlogCategoryPageReq req) {
    List<BlogCategoryResp> blogCategoryList = cacheService.getBlogCategoryListCache(CACHE_KEY_BLOG_CATEGORY_LIST);
    if (CollectionUtils.isEmpty(blogCategoryList)) {
      blogCategoryList = blogCategoryMapper.categoryList(req);
      cacheService.saveBlogCategoryCache(blogCategoryList);
    }

    if (CollectionUtils.isEmpty(blogCategoryList)) {
      return PageResult.emptyPage();
    }

    return new PageResult<>(blogCategoryList, blogCategoryList.size());
  }

  @Override
  public ApiResp<String> add(BlogCategoryReq req) {
    BlogCategory blogCategoryRes = blogCategoryMapper.selectByNumber(req.getNumber());
    if (Objects.nonNull(blogCategoryRes)) {
        return ApiResp.failure(DATA_INFO_REPEAT);
    }else {
        blogCategoryRes = BlogCategory.builder().build();
    }

    BlogCategory saveEntity = BlogCategoryDTO.convertSaveCategoryReq(req, blogCategoryRes);
    Integer save = blogCategoryMapper.insert(saveEntity);
    if (save >= 1) {
      // 更新缓存
      BlogCategoryResp blogCategoryResp = new BlogCategoryResp();
      BeanUtils.copyProperties(saveEntity, blogCategoryResp);
      cacheService.updateBlogCategoryCache(CACHE_KEY_BLOG_CATEGORY_LIST, blogCategoryResp, BUS_CREATE);
      return ApiResp.success();
    }else {
        return ApiResp.failure(ADD_ERROR);
    }
  }

    @Override
    public ApiResp<String> edit(BlogCategoryReq req) {
      BlogCategory before = blogCategoryMapper.selectBySurrogateId(req.getSurrogateId());
      if (Objects.isNull(before)) {
          return ApiResp.failure(OPERATE_ERROR);
      }

      if (!before.getNumber().equalsIgnoreCase(req.getNumber())) {
          return ApiResp.failure(OPERATE_ERROR);
      }

      BeanUtils.copyProperties(req, before);
      Date nowDateTime = DateUtil.localDateTimeToDate(LocalDateTime.now());
      before.setStatus(0); // default 0, it not use now
      before.setUpdateTime(nowDateTime);
      before.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
      Integer count = blogCategoryMapper.editBySurrogateId(before);
      if (count >= 1) {
        // 更新缓存
        BlogCategoryResp blogCategoryResp = new BlogCategoryResp();
        BeanUtils.copyProperties(before, blogCategoryResp);
        cacheService.updateBlogCategoryCache(CACHE_KEY_BLOG_CATEGORY_LIST, blogCategoryResp, BUS_EDIT);
        return ApiResp.success();
      }else {
        return ApiResp.failure(ADD_ERROR);
      }
    }

    @Override
    public ApiResp<String> delete(Long surrogateId) {
      int count = blogCategoryMapper.deleteBySurrogateId(surrogateId);
      if (count >= 1) {
        // 更新缓存
        BlogCategoryResp blogCategoryResp = new BlogCategoryResp();
        blogCategoryResp.setSurrogateId(surrogateId);
        cacheService.updateBlogCategoryCache(CACHE_KEY_BLOG_CATEGORY_LIST, blogCategoryResp, BUS_DELETE);
        return ApiResp.success("删除成功");
      }else {
        return ApiResp.failure(OPERATE_ERROR);
      }
    }

  @Override
  public ApiResp<String> deleteBatch(BlogCategoryReq req) {
    Integer count = blogCategoryMapper.deleteBatch(req.getSurrogateIds());
    if (count >= 1) {
      return ApiResp.success();
    }else {
      return ApiResp.failure(DEL_ERROR);
    }
  }

  @Override
  public List<BlogCategoryResp> frontList() {
    List<BlogCategoryResp> res = blogCategoryMapper.frontList();

    return res;
  }
}
