package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.admin.pojo.req.blog.diary.DiaryPageListReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.diary.DiarySaveReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogDiaryPageListResp;

/**
 * @Author: Lil-K
 * @Date: 2025/5/8
 * @Description:
 */
public interface BlogDiaryService {

  PageResult<BlogDiaryPageListResp> pageDiaryList(DiaryPageListReq req);

  ApiResp<String> add(DiarySaveReq diarySaveReq);

  ApiResp<String> edit(DiarySaveReq diarySaveReq);

  ApiResp<String> delete(Long id);
}