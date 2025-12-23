package org.cy.micoservice.blog.admin.pojo.dto.blog;

import org.cy.micoservice.blog.admin.common.holder.RequestHolder;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogTopic;
import org.cy.micoservice.blog.admin.pojo.req.blog.topic.BlogTopicReq;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: Lil-K
 * @Date: 2024/4/7
 * @Description:
 */
public class BlogTopicDTO {

  public static BlogTopic convertSaveTopicReq(BlogTopicReq blogTopicReq) {
    BlogTopic blogTopic = BlogTopic.builder().build();
    BeanUtils.copyProperties(blogTopicReq, blogTopic);

    blogTopic.setSurrogateId(IdWorker.getSnowFlakeId());
    Date nowDateTime = DateUtil.localDateTimeToDate(LocalDateTime.now());

    blogTopic.setCreatorId(RequestHolder.getCurrentUser().getSurrogateId());
    blogTopic.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
    blogTopic.setCreateTime(nowDateTime);
    blogTopic.setUpdateTime(nowDateTime);

    return blogTopic;
  }

}
