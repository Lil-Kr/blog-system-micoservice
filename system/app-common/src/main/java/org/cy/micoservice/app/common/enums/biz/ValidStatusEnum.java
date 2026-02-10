package org.cy.micoservice.app.common.enums.biz;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/6/8
 * @Description: 公共 ValidStatusEnum,  生效 / 不生效 枚举
 */
@Getter
@AllArgsConstructor
public enum ValidStatusEnum {

  ACTIVE(0, "生效"),
  INACTIVE(1, "未生效"),
  ;

  Integer code;
  String desc;
}