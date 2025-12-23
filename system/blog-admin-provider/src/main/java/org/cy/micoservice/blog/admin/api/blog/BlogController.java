package org.cy.micoservice.blog.admin.api.blog;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.web.starter.annotations.CheckAuth;
import org.cy.micoservice.blog.framework.web.starter.annotations.RecordLogger;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogRichEditorResp;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogContentPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogContentReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogRichEditorImageReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogContentResp;
import org.cy.micoservice.blog.admin.service.BlogContentService;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2024/4/24
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/blog/content")
public class BlogController {

  @Autowired
  private BlogContentService blogContentService;

  @RecordLogger
  @CheckAuth
  @PostMapping("/pageList")
  public ApiResp<PageResult<BlogContentResp>> pageContentList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) BlogContentPageReq req) {
    PageResult<BlogContentResp> list = blogContentService.pageContentList(req);
    return ApiResp.success(list);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/list")
  public ApiResp<PageResult<BlogContentResp>> list(@RequestBody @Validated BlogContentPageReq req) {
    PageResult<BlogContentResp> list = blogContentService.contentList(req);
    return ApiResp.success(list);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/publish")
  public ApiResp<String> publishBlog(@RequestBody @Validated({BlogContentReq.GroupBlogContentPublish.class}) BlogContentReq req) {
    return blogContentService.publishBlog(req);
  }

  @RecordLogger
  @CheckAuth
  @GetMapping("/getContent/{blogId}")
  public ApiResp<BlogContentResp> getContent(@PathVariable("blogId") @Valid @NotNull(message = "blogId是必须的") Long blogId) {
    return blogContentService.getContent(blogId);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/add")
  public ApiResp<String> add(@RequestBody @Validated({BlogContentReq.GroupBlogContentAdd.class}) BlogContentReq req) {
    return blogContentService.add(req);
  }

  @RecordLogger
  @CheckAuth
  @PostMapping("/edit")
  public ApiResp<String> edit(@RequestBody @Validated({BlogContentReq.GroupBlogContentEdit.class}) BlogContentReq req) {
    return blogContentService.edit(req);
  }

  @RecordLogger
  @CheckAuth
  @DeleteMapping("/delete")
  public ApiResp<String> delete(@RequestParam("surrogateId") @Valid @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return blogContentService.delete(surrogateId);
  }

  /**
   * blog rich editor img upload
   * @param req
   * @return
   * @throws Exception
   */
  @RecordLogger
  @CheckAuth
  @PostMapping("/upload")
  public ApiResp<BlogRichEditorResp> upload(@ModelAttribute BlogRichEditorImageReq req) throws Exception {
    return blogContentService.uploadBlogContentImage(req);
  }

  /** ================== Portal API =============== **/
  @RecordLogger
  @GetMapping("/frontContentList")
  public ApiResp<List<BlogContentResp>> frontContentList() {
    return blogContentService.frontContentList();
  }

  @RecordLogger
  @PostMapping("/frontContentPageList")
  public ApiResp<PageResult<BlogContentResp>> frontContentPageList(@RequestBody @Validated({BasePageReq.GroupPageQuery.class}) BlogContentPageReq req) {
    PageResult<BlogContentResp> list = blogContentService.frontContentPageList(req);
    return ApiResp.success(list);
  }

  @RecordLogger
  @GetMapping("/frontGetBlog")
  public ApiResp<BlogContentResp> frontGetBlog(@RequestParam("surrogateId") @Valid @NotNull(message = "surrogateId是必须的") Long surrogateId) {
    return blogContentService.getBlog(surrogateId);
  }
}
