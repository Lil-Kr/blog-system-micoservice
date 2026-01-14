package org.cy.micoservice.blog.admin.provider.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.blog.BlogCategory;
import org.cy.micoservice.blog.entity.admin.model.req.blog.category.BlogCategoryPageReq;
import org.cy.micoservice.blog.entity.admin.model.resp.blog.BlogCategoryResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/28
 * @Description:
 */
@Repository
public interface BlogCategoryMapper extends BaseMapper<BlogCategory> {

  Integer editBySurrogateId(@Param("param") BlogCategory req);

  BlogCategory selectBySurrogateId(Long surrogateId);

  BlogCategory selectByNumber(String Number);

  List<BlogCategoryResp> pageCategoryList(@Param("param") BlogCategoryPageReq req);

  List<BlogCategoryResp> categoryList(@Param("param") BlogCategoryPageReq req);

  Integer getCountByList(@Param("param") BlogCategoryPageReq req);

  Integer deleteBySurrogateId(Long surrogateId);

  Integer deleteBatch(List<Long> surrogateIds);

  List<BlogCategoryResp> frontList();

}
