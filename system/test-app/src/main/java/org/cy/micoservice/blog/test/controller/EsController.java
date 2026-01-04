package org.cy.micoservice.blog.test.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.elasticsearch.starter.utils.ElasticsearchUtil;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.test.config.TestApplicationProperties;
import org.cy.micoservice.blog.test.pojo.UserFollowRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/es")
public class EsController {

  @Autowired
  private TestApplicationProperties applicationProperties;

  @Resource
  private ElasticsearchUtil elasticsearchUtil;

  @NoAuthCheck
  @GetMapping("/get")
  public UserFollowRelation get() {
    UserFollowRelation documentById = elasticsearchUtil.getDocumentById(applicationProperties.getEsRecordIndexAliasUserFollowerRelation(), "1", UserFollowRelation.class);
    return documentById;
  }
}