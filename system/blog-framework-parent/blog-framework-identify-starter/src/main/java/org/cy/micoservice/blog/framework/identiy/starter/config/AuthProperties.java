package org.cy.micoservice.blog.framework.identiy.starter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description:
 */
@Data

public class AuthProperties {

  @Value("${blog.identify.secret-key:}")
  private String secretKey;

  @Value("${blog.identify.access-token.expire-time:}")
  private Long accessTokenExpireTime;

  @Value("${blog.identify.auth.template:}")
  private String authType;
}
