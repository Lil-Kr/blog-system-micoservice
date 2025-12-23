package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogLabel;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelListReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogLabelResp;

/**
 * @Author Lil-K
 * @since 2024-03-31
 */
public interface BlogLabelService {

	PageResult<BlogLabelResp> pageList(BlogLabelPageReq req);

	PageResult<BlogLabel> list(BlogLabelListReq req);

	ApiResp<String> add(BlogLabelReq req);

	ApiResp<String> edit(BlogLabelReq req);

	ApiResp<String> delete(BlogLabelReq req);

	ApiResp<String> deleteBatch(BlogLabelReq req);
}
