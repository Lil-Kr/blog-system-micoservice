package org.cy.micoservice.app.user.provider.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Data
@Configuration
@RefreshScope
public class UserApplicationProperties {

  @Value("${es.user.follower.index:blog.user.user-follower-relation}")
  private String userFollowerEsIndexName;

  @Value("${es.user.phone.index:blog.user.user-phone-relation}")
  private String userPhoneEsIndexName;
}