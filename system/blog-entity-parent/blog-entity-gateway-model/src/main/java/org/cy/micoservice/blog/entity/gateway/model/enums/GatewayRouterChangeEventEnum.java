package org.cy.micoservice.blog.entity.gateway.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description: gateway statue enum
 */
@Getter
@AllArgsConstructor
public enum GatewayRouterChangeEventEnum {

  INSERT("INSERT", "新增"),
  UPDATE("UPDATE", "修改"),
  DELETED("DELETE", "删除"),
  ;

  String code;
  String name;

}