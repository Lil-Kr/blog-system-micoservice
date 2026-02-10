package org.cy.micoservice.app.framework.identiy.starter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description:
 */
@Data
public class AuthProperties {

  @Value("${identify.secret-key:}")
  private String secretKey;

  @Value("${identify.access-token.expire-time:}")
  private Long accessTokenExpireTime;

  /**
   * 认证类型, 默认为JWT
   */
  @Value("${identify.auth.template:}")
  private String authType;
}
