package org.cy.micoservice.blog.admin.provider.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.blog.BlogTopic;
import org.cy.micoservice.blog.entity.admin.model.req.blog.topic.BlogTopicPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.blog.topic.BlogTopicReq;
import org.cy.micoservice.blog.entity.admin.model.resp.blog.BlogTopicResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @since 2024-03-31
 */
@Repository
public interface BlogTopicMapper extends BaseMapper<BlogTopic> {

  List<BlogTopicResp> pageTopicList(@Param("param") BlogTopicPageReq req);

  List<BlogTopic> topicList(@Param("param") BlogTopicReq req);

  BlogTopic selectByNumber(String number);
}