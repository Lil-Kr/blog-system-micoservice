package org.cy.micoservice.blog.admin.pojo.dto.blog;


import org.cy.micoservice.blog.admin.common.holder.RequestHolder;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogContent;
import org.cy.micoservice.blog.admin.pojo.req.blog.content.BlogContentReq;
import org.cy.micoservice.blog.admin.utils.keyUtil.RunCodeUtil;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2024/5/24
 * @Description:
 */
public class BlogContentDTO {

  public static BlogContent convertAddBlogContentReq(BlogContentReq baseReq) {
    BlogContent blogContent = new BlogContent();
    BeanUtils.copyProperties(baseReq, blogContent);
    blogContent.setSurrogateId(IdWorker.getSnowFlakeId());
    blogContent.setNumber(RunCodeUtil.getFourPipelineNumbers("blog-"));
    blogContent.setLabelIds(convertBlogLabelToString(baseReq.getLabelIds()));
    blogContent.setDeleted(0);

    blogContent.setCreatorId(RequestHolder.getCurrentUser().getSurrogateId());
    blogContent.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    Date nowDateTime = DateUtil.localDateTimeToDate(LocalDateTime.now());
    blogContent.setPublishTime(nowDateTime);
    blogContent.setCreateTime(nowDateTime);
    blogContent.setUpdateTime(nowDateTime);

    return blogContent;
  }

  public static String convertBlogLabelToString(Set<String> labelIds) {
    return labelIds.stream().filter(Objects::nonNull)
      .distinct() // remove duplicate label
      .map(String::valueOf)
      .collect(Collectors.joining(","));
  }



}