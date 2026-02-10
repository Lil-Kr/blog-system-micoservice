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
public enum GatewayRouterDeletedEnum {

  ACTIVE(0, "活跃"),
  DELETED(1, "已删除"),
  ;

  Integer code;
  String desc;
}