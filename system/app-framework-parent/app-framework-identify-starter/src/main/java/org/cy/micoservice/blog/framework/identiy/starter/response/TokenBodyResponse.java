package org.cy.micoservice.blog.framework.identiy.starter.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: token 内部信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenBodyResponse {

  private String subject;

  private Map<String, Object> authAttrs;
}