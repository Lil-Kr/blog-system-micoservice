package org.cy.micoservice.blog.message.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ChatRecordStatusEnum {

  CHECKING(0,"待审核"),
  VALID(1,"合法"),
  INVALID(2,"不合法")
  ;
  int code;
  String desc;
}