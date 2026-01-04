package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogContentMongo;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogRichEditorResp;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogContentPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogContentReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogRichEditorImageReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogContentGroupResp;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogContentResp;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author: Lil-K
 * @since 2024-03-31
 */
public interface BlogContentService {

  ApiResp<String> add(BlogContentReq req);

  BlogContentMongo saveBlogContentMongo(BlogContentMongo entity);

  PageResult<BlogContentResp> pageContentList(BlogContentPageReq req);

  PageResult<BlogContentResp> contentList(BlogContentPageReq req);

  ApiResp<BlogContentResp> getBlog(Long surrogateId);

  ApiResp<String> edit(BlogContentReq req);

  ApiResp<String> publishBlog(BlogContentReq req);

  ApiResp<BlogContentResp> getContent(Long blogId);

  ApiResp<List<BlogContentResp>> frontContentList();

  List<BlogContentGroupResp> frontContentByGroupCategory();

  PageResult<BlogContentResp> frontContentPageList(BlogContentPageReq req);

	ApiResp<String> delete(Long surrogateId);

  ApiResp<BlogRichEditorResp> uploadBlogContentImage(BlogRichEditorImageReq req) throws Exception;
}
