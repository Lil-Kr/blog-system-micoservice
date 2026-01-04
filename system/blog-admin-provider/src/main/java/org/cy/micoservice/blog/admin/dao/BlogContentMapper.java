package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogContent;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogContentPageReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogContentGroupResp;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogContentResp;
import org.cy.micoservice.blog.admin.pojo.resp.blog.PrevAndNext;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author: Lil-K
 * @since 2024-03-31
 */
@Repository
public interface BlogContentMapper extends BaseMapper<BlogContent> {

  List<BlogContentResp> pageContentList(@Param("param") BlogContentPageReq req);

  Integer pageContentCount(@Param("param") BlogContentPageReq req);

  List<BlogContentResp> contentList(@Param("param") BlogContentPageReq req);

  Integer updateStatusBySurrogateId(@Param("param") BlogContent req);

  List<BlogContentResp> frontContentList();

  List<BlogContentGroupResp> frontContentByGroupCategory();

  List<BlogContentResp> pageFrontContentList(@Param("param") BlogContentPageReq req);

  Integer pageFrontContentCount(@Param("param") BlogContentPageReq req);

  PrevAndNext prevBlog(Long blogId);

  PrevAndNext nextBlog(Long blogId);
}