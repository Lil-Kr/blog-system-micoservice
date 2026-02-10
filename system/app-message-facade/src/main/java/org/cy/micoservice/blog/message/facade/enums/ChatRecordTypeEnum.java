package org.cy.micoservice.blog.message.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ChatRecordTypeEnum {

  TEXT(1,"文本消息"),
  ;
  Integer code;
  String desc;
}