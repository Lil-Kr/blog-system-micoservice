package org.cy.micoservice.blog.framework.identiy.starter.template;

import org.cy.micoservice.blog.framework.identiy.starter.request.CreateTokenRequest;
import org.cy.micoservice.blog.framework.identiy.starter.response.CreateTokenResponse;
import org.cy.micoservice.blog.framework.identiy.starter.response.TokenBodyResponse;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description:
 */
public interface AuthTemplate {

  /**
   * 认证模板初始化
   */
  void init();

  /**
   * 创建 token
   * @param request
   * @return
   */
  CreateTokenResponse createToken(CreateTokenRequest request);

  /**
   * 校验 token 合法性
   * @param token
   * @return
   */
  boolean checkToken(String token);

  /**
   * 获取 token 信息
   * @param token
   * @return
   */
  TokenBodyResponse extraAttrsFromToken(String token);

  /**
   * 获取 token 的 subject 值
   * @param authorization
   * @return
   */
  String getSubjectFromToken(String authorization);
}