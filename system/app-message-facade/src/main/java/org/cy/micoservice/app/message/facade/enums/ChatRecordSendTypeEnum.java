package org.cy.micoservice.app.message.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ChatRecordSendTypeEnum {

  SENT("sent"),
  RECEIVER("received")
    ;
  String code;
}
