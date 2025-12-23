package org.cy.micoservice.blog.framework.identiy.starter.template.impl;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.framework.identiy.starter.config.AuthProperties;
import org.cy.micoservice.blog.framework.identiy.starter.request.CreateTokenRequest;
import org.cy.micoservice.blog.framework.identiy.starter.response.CreateTokenResponse;
import org.cy.micoservice.blog.framework.identiy.starter.response.TokenBodyResponse;
import org.cy.micoservice.blog.framework.identiy.starter.template.AbstractAuthTemplate;
import org.cy.micoservice.blog.framework.identiy.starter.uitls.JWTUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: 基于 jwt 的认证模板
 */
@Slf4j
public class JWTAuthTemplateImpl extends AbstractAuthTemplate {

  private AuthProperties authProperties;

  public JWTAuthTemplateImpl(AuthProperties authProperties) {
    this.authProperties = authProperties;
  }

  @Override
  public void init() {
    String serretKey = authProperties.getSecretKey();
    if (StringUtils.isBlank(serretKey) || serretKey.length() < 50) {
      throw new RuntimeException("invalid serretKey's length is less than 50");
    }
  }

  @Override
  protected void beforeCreateToken(CreateTokenRequest request) {
    log.info("before create token request: {}", request);
  }

  @Override
  protected void afterCreateToken(CreateTokenRequest request) {

  }

  @Override
  protected void beforeVerifyToken(String token) {
    log.info("before check token: {}", token);
  }

  @Override
  protected void afterVerifyToken(String token) {
    log.info("after check token: {}", token);
  }

  @Override
  protected CreateTokenResponse generateToken(CreateTokenRequest request) {
    long expireTimeMs = System.currentTimeMillis() + authProperties.getAccessTokenExpireTime();
    String token = JWTUtil.generateToken(request.getSubject(),
      request.getAuthAttrs(),
      authProperties.getSecretKey(),
      authProperties.getAccessTokenExpireTime());
    return new CreateTokenResponse(token, expireTimeMs);
  }

  @Override
  protected boolean doVerifyToken(String token) {
    return JWTUtil.validateToken(token, authProperties.getSecretKey());
  }

  @Override
  public TokenBodyResponse extraAttrsFromToken(String token) {
    Claims claims = JWTUtil.extractAllClaims(token, authProperties.getSecretKey());
    Map<String, Object> attrs = new HashMap<>();
    for (String key : claims.keySet()) {
      attrs.put(key, claims.get(key));
    }
    String subject = JWTUtil.extractSubject(token, authProperties.getSecretKey());
    return new TokenBodyResponse(subject, attrs);
  }

  @Override
  public String getSubjectFromToken(String authorization) {
    return JWTUtil.extractSubject(authorization, authProperties.getSecretKey());
  }
}
