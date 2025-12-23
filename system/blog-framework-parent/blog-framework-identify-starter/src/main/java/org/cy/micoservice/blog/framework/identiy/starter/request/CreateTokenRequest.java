package org.cy.micoservice.blog.framework.identiy.starter.request;

import lombok.Data;

import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: 创建 token 请求体
 */
@Data
public class CreateTokenRequest {

  /**
   * 唯一身份标识
   */
  private String subject;

  /**
   * 认证使用的上下文信息
   */
  private Map<String, Object> authAttrs;

  private String issuer;
}