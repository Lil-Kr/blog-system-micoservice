package org.cy.micoservice.blog.common.enums.biz;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2026/1/4
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum AuthTypeEnum {
  JWT("jwt", "JWT认证"),
  OTHER("other", "其他认证"),
    ;

  String code;
  String desc;
}
