package org.cy.micoservice.blog.admin.facade.dto.sys.admin;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Data
public class AdminLoginAdminReqDTO implements Serializable {
  @Serial
  private static final long serialVersionUID = 7099147861341741962L;

  private String token;

  private String account;

  private String password;

  private String email;
}
