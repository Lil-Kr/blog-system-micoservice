package org.cy.micoservice.app.gateway.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description: gateway statue enum
 */
@Getter
@AllArgsConstructor
public enum GatewayRouterSchemaEnum {
  HTTP("http", "http协议"),
  DUBBO("dubbo", "dubbo协议"),
  ;

  String code;
  String desc;

  public static GatewayRouterSchemaEnum getByCode(String code) {
    for (GatewayRouterSchemaEnum gatewayRouterSchemaEnum : GatewayRouterSchemaEnum.values()) {
      if (gatewayRouterSchemaEnum.getCode().equals(code)) {
        return gatewayRouterSchemaEnum;
      }
    }
    return null;
  }
}