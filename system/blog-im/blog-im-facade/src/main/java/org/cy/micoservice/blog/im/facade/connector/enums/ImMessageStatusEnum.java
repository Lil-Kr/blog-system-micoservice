package org.cy.micoservice.blog.im.facade.connector.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImMessageStatusEnum {

  IDENTIFY_SUCCESS((short) 1, "认证成功"),
  SEND_BIZ_MSG_SUCCESS((short)1, "发送业务消息成功"),
  ;

  short code;
  String desc;
}
