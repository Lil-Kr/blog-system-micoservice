package org.cy.micoservice.blog.admin.service.impl;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.admin.dao.BlogDiaryMapper;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogDiary;
import org.cy.micoservice.blog.admin.pojo.req.blog.diary.DiaryPageListReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.diary.DiarySaveReq;
import org.cy.micoservice.blog.admin.pojo.dto.blog.BlogDiaryDTO;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogDiaryPageListResp;
import org.cy.micoservice.blog.admin.service.BlogDiaryService;
import org.cy.micoservice.blog.admin.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import static org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum.*;

/**
 * @Author: Lil-K
 * @Date: 2025/5/8
 * @Description:
 */
@Service
@Slf4j
public class BlogDiaryServiceImpl implements BlogDiaryService {

  @Autowired
  private BlogDiaryMapper diaryMapper;

  @Autowired
  private CacheService cacheService;

  @Override
  public PageResult<BlogDiaryPageListResp> pageDiaryList(DiaryPageListReq req) {
    List<BlogDiaryPageListResp> pageList = diaryMapper.pageDiaryList(req);
    pageList.forEach(diary -> {
      diary.setCreatorName(cacheService.getUserAdminIdCache(diary.getCreatorId()).getAccount());
      diary.setOperatorName(cacheService.getUserAdminIdCache(diary.getCreatorId()).getAccount());
    });
    Integer count = diaryMapper.countPageDiaryList(req);

    if (CollectionUtils.isEmpty(pageList)) {
      return new PageResult<>(new ArrayList<>(0), 0);
    }
    return new PageResult<>(pageList, count);
  }

  @Override
  public ApiResp<String> add(DiarySaveReq req) {
    BlogDiary diary = BlogDiaryDTO.convertAddDiaryEntity(req);

    int insert = diaryMapper.insertSelective(diary);
    if (insert < 1) {
      return ApiResp.failure(ADD_ERROR);
    }
    return ApiResp.success();
  }

  @Override
  public ApiResp<String> edit(DiarySaveReq req) {
    BlogDiary diary = BlogDiaryDTO.convertEditDiaryEntity(req);

    int update = diaryMapper.updateById(diary);
    if (update < 1) {
      return ApiResp.failure(UPDATE_ERROR);
    }
    return ApiResp.success();
  }

  @Override
  public ApiResp<String> delete(Long id) {
    int del = diaryMapper.deleteById(id);
    if (del < 1) {
      return ApiResp.failure(DEL_ERROR);
    }
    return ApiResp.success();
  }
}
