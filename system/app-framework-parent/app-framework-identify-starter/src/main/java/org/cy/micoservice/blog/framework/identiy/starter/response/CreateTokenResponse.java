package org.cy.micoservice.blog.framework.identiy.starter.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTokenResponse {

  /**
   * token 信息
   */
  private String token;

  /**
   * 过期时间
   */
  private Long expireAt;

}