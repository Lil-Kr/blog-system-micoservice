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
public enum ChatRelationTypeEnum {

  SINGLE_CHAT(1,"私聊"),
  GROUP_CHAT(1,"群聊"),
    ;

  int code;
  String desc;
}
