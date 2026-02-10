package org.cy.micoservice.app.message.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ChatMsgStatusEnum {

  SUCCESS(1,"成功"),
  SYSTEM_ERROR(2,"系统异常"),
  INVALID_BODY(3,"非法内容"),
  WAITING_CHECK_RESP(4,"等待审核结果"),
  ;

  int code;
  String desc;
}
