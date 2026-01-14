package org.cy.micoservice.blog.admin.provider.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.image.ImageCategory;
import org.cy.micoservice.blog.entity.admin.model.req.image.ImageCategoryListReq;
import org.cy.micoservice.blog.entity.admin.model.req.image.ImageCategoryPageListReq;
import org.cy.micoservice.blog.entity.admin.model.resp.image.ImageCategoryResp;
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