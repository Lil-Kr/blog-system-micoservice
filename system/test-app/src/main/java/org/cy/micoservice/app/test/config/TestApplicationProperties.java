package org.cy.micoservice.app.test.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Data
@Configuration
public class TestApplicationProperties {

  @Value("${es.user.follower.index:blog.user.user-follower-relation}")
  private String esRecordIndexAliasUserFollowerRelation;
}