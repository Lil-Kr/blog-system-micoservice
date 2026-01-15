package org.cy.micoservice.blog.common.enums.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2026/1/15
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum AdminStatueEnum {
  NORMAL(0, "正常"),
  FREEZE(1, "冻结"),
  DELETE(2, "删除"),
  ;

  int code;
  String desc;
}
