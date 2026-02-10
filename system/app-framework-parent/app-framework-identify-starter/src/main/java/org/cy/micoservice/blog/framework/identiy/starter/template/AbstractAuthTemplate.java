package org.cy.micoservice.blog.framework.identiy.starter.template;

import org.cy.micoservice.blog.framework.identiy.starter.request.CreateTokenRequest;
import org.cy.micoservice.blog.framework.identiy.starter.response.CreateTokenResponse;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description:
 */
public abstract class AbstractAuthTemplate implements AuthTemplate {

  /**
   * 创建 token 之前做的操作
   * @param request
   */
  protected abstract void beforeCreateToken(CreateTokenRequest request);

  /**
   * 创建 token 之后做的操作
   * @param request
   */
  protected abstract void afterCreateToken(CreateTokenRequest request);

  /**
   * 验证 token 之前做的操作
   * @param token
   */
  protected abstract void beforeVerifyToken(String token);

  /**
   * 验证 token 之后做的操作
   * @param token
   */
  protected abstract void afterVerifyToken(String token);

  protected abstract CreateTokenResponse generateToken(CreateTokenRequest request);

  protected abstract boolean doVerifyToken(String token);

  @Override
  public CreateTokenResponse createToken(CreateTokenRequest request) {
    try {
      this.beforeCreateToken(request);
      return this.generateToken(request);
    } finally {
      this.afterCreateToken(request);
    }
  }

  @Override
  public boolean checkToken(String token) {
    try {
      this.beforeVerifyToken(token);
      return this.doVerifyToken(token);
    } finally {
      this.afterVerifyToken(token);
    }
  }
}
