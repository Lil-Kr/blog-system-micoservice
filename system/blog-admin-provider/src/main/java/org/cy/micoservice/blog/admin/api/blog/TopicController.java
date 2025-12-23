package org.cy.micoservice.blog.admin.api.blog;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogTopic;
import org.cy.micoservice.blog.admin.pojo.req.blog.topic.BlogTopicPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.topic.BlogTopicReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogTopicResp;
import org.cy.micoservice.blog.admin.service.BlogTopicService;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Lil-K
 * @Date: 2024/5/25
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/blog/topic")
public class TopicController {

  @Autowired
  private BlogTopicService blogTopicService;

  @RecordLogger
  @CheckAuth
  @PostMapping("/pageList")
  public ApiResp<PageResult<BlogTopicResp>> pageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) BlogTopicPageReq req) {
    PageResult<BlogTopicResp> blogTopicVOPageResult = blogTopicService.pageList(req);
    return ApiResp.success(blogTopicVOPageResult);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/list")
  public ApiResp<PageResult<BlogTopic>> list(@RequestBody @Valid BlogTopicReq req) {
    PageResult<BlogTopic> blogTopicVOPageResult = blogTopicService.list(req);
    return ApiResp.success(blogTopicVOPageResult);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated(BlogTopicReq.GroupBlogTopicSave.class) BlogTopicReq req) {
    return blogTopicService.add(req);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated(BlogTopicReq.GroupBlogTopicEdit.class) BlogTopicReq req) {
    return blogTopicService.edit(req);
  }

  @RecordLogger
  @CheckAuth
  @DeleteMapping("/delete")
  public ApiResp<String> delete(@RequestParam("surrogateId") @Valid @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return blogTopicService.delete(surrogateId);
  }
}