package org.cy.micoservice.blog.audit.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum AuditRefTypeEnum {

  NOTE_TEXT(1,"笔记文本检测"),
  CHAT_TEXT(2,"聊天会话文本检测"),
  ;

  Integer code;
  String desc;
}