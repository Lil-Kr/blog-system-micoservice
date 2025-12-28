package org.cy.micoservice.blog.admin.api.blog;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.admin.pojo.req.blog.diary.DiaryPageListReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.diary.DiarySaveReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogDiaryPageListResp;
import org.cy.micoservice.blog.admin.service.BlogDiaryService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Lil-K
 * @Date: 2025/5/8
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/blog/diary")
public class DiaryController {

  @Autowired
  private BlogDiaryService diaryService;

  @CheckAuth
  @RecordLogger
  @PostMapping("/pageList")
  public ApiResp<PageResult<BlogDiaryPageListResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) DiaryPageListReq req) {
    PageResult<BlogDiaryPageListResp> pageList = diaryService.pageDiaryList(req);
    return ApiResp.success(pageList);
  }

  @CheckAuth
  @RecordLogger
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated(DiarySaveReq.GroupAdd.class) DiarySaveReq req) {
    return diaryService.add(req);
  }

  @CheckAuth
  @RecordLogger
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated(DiarySaveReq.GroupEdit.class) DiarySaveReq req) {
    return diaryService.edit(req);
  }

  @CheckAuth
  @RecordLogger
  @DeleteMapping("/delete/{id}")
  public ApiResp<String> delete(@PathVariable("id") @Valid @NotNull(message = "id cant not be null") Long id) {
    return diaryService.delete(id);
  }

  /** ================== 门户网站 [时间轴] 接口 =============== **/

  @RecordLogger
  @GetMapping("/frontContentList/{currentPageNum}/{pageSize}")
  public ApiResp<PageResult<BlogDiaryPageListResp>> frontDiaryList(
    @PathVariable("currentPageNum")
    @NotNull(groups = {BasePageReq.GroupPageQuery.class}, message = "current page number cant not be null")
    @Min(groups = {BasePageReq.GroupPageQuery.class}, value = 1, message ="page number cant not less than 1")
    @Max(groups = {BasePageReq.GroupPageQuery.class}, value = 10, message ="page number cant not greater than 10") Integer currentPageNum,

    @PathVariable("pageSize")
    @NotNull(groups = {BasePageReq.GroupPageQuery.class}, message = "page size cant not be null")
    @Max(groups = {BasePageReq.GroupPageQuery.class}, value = 100, message = "page size cant not greater than 100") Integer pageSize
  ) {
    DiaryPageListReq req = new DiaryPageListReq();
    req.setCurrentPageNum(currentPageNum);
    req.setPageSize(pageSize);
    PageResult<BlogDiaryPageListResp> pageList = diaryService.pageDiaryList(req);
    return ApiResp.success(pageList);
  }
}