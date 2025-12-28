package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogTopic;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogTopicResp;
import org.cy.micoservice.blog.admin.pojo.req.blog.topic.BlogTopicPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.topic.BlogTopicReq;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author Lil-K
 * @since 2024-03-31
 */
public interface BlogTopicService {

  PageResult<BlogTopicResp> pageList(BlogTopicPageReq req);

  PageResult<BlogTopic> list(BlogTopicReq req);

  ApiResp<String> add(BlogTopicReq req);

  ApiResp<String> edit(BlogTopicReq req);

  ApiResp<String> delete(Long surrogateId);

  ApiResp<String> deleteBatch(BlogTopicReq req);
}
