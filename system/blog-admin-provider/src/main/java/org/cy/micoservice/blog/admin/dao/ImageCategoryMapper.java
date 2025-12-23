package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cy.micoservice.blog.admin.pojo.entity.image.ImageCategory;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageCategoryListReq;
import org.cy.micoservice.blog.admin.pojo.req.image.ImageCategoryPageListReq;
import org.cy.micoservice.blog.admin.pojo.resp.image.ImageCategoryResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ImageCategoryDAO继承基类
 */
@Repository
public interface ImageCategoryMapper extends BaseMapper<ImageCategory> {

  List<ImageCategoryResp> pageList(@Param("param") ImageCategoryPageListReq req);

  Integer total(@Param("param") ImageCategoryPageListReq req);

  List<ImageCategoryResp> imageCategoryList(@Param("param") ImageCategoryListReq req);

  ImageCategoryResp get(Long surrogateId);

  List<ImageCategoryResp> imageCategoryNameList(@Param("param") ImageCategoryListReq req);
}