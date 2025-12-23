package org.cy.micoservice.blog.audit.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 审核类型枚举
 */
@Getter
@AllArgsConstructor
public enum AuditTypeEnum {

  TEXT(1,"text"),
  IMAGE(2,"图片"),
  ;

  Integer code;
  String desc;
}