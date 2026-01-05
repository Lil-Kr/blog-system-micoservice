package org.cy.micoservice.blog.framework.identiy.starter.config;

import org.cy.micoservice.blog.framework.identiy.starter.constant.IdentifyConstants;
import org.cy.micoservice.blog.framework.identiy.starter.template.AuthTemplate;
import org.cy.micoservice.blog.framework.identiy.starter.template.impl.JWTAuthTemplateImpl;
import org.springframework.context.annotation.Bean;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description:
 */
public class AuthTemplateConfig {

  @Bean
  public AuthProperties authProperties () {
    return new AuthProperties();
  }

  @Bean
  public AuthTemplate authTemplate (AuthProperties authProperties) {
    if (IdentifyConstants.JWT.endsWith(authProperties.getAuthType())) {
      return new JWTAuthTemplateImpl(authProperties);
    }

    throw new IllegalArgumentException("invalid authTemplate type");
  }
}