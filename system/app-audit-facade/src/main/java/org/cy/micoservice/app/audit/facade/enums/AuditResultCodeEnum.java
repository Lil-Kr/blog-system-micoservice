package org.cy.micoservice.app.audit.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum AuditResultCodeEnum {

  VALID(0, "审核通过"),
  INVALID_DATA(1,"数据异常"),
  THIRD_CHECK_INVALID(2,"第三方检测异常"),
  USER_CHECK_INVALID(3,"用户检测异常"),
  ;

  Integer code;
  String value;
}