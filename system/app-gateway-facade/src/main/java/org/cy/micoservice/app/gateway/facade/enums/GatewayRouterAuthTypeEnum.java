package org.cy.micoservice.app.gateway.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum GatewayRouterAuthTypeEnum {

  JWT("jwt", "jwt方式解析"),
  NONE("none", "游客状态, 不需要鉴权");


  private String code;
  private String desc;
}