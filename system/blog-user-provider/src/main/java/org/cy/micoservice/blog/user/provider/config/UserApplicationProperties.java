package org.cy.micoservice.blog.user.provider.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Configuration
@RefreshScope
@Data
public class UserApplicationProperties {

  @Value("${es.user.follower.index:blog.user.user-follower-relation}")
  private String userFollowerEsIndexName;
}