package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogTopic;
import org.cy.micoservice.blog.admin.pojo.req.blog.topic.BlogTopicPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.topic.BlogTopicReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogTopicResp;
import org.apache.ibatis.annotations.Param;
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