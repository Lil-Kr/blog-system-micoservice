package org.cy.micoservice.app.user.facade.provider.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/31
 * @Description: 用户进入程序内部的初始化信号DTO
 */
@Data
public class UserEnterInitReqDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 8984456517680815450L;

  private Long userId;

  private Long eventTime;

  private String userAgent;

  private String referer;
}