package org.cy.micoservice.blog.gateway.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description: gateway statue enum
 */
@Getter
@AllArgsConstructor
public enum GatewayRouterStatusEnum {
  VALID(0, "生效"),
  INVALID(1, "不可用"),
  ;

  Integer code;
  String desc;
}