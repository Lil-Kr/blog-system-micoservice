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
public enum TextAuditBodyTypeEnum {

  NOTE(1,"note"),
  CHAT(2,"chat"),
  ;

  Integer code;
  String desc;
}
