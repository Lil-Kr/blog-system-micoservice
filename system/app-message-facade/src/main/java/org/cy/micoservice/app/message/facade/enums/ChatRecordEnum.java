package org.cy.micoservice.app.message.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ChatRecordEnum {

  TEXT(1,"文本消息"),
  ;
  int code;
  String desc;
}