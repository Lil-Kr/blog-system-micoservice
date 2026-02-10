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
public enum ChatRelationStatusEnum {

  VALID(1,"有效"),
  SHIELD(2,"屏蔽"),
    ;
  int code;
  String desc;
}