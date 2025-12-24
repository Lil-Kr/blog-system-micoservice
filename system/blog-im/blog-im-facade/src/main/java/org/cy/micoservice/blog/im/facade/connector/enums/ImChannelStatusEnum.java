package org.cy.micoservice.blog.im.facade.connector.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/12/12
 * @Description: channel通道的状态枚举
 */
@Getter
@AllArgsConstructor
public enum ImChannelStatusEnum {

  WAITING_FOR_IDENTIFY(0,"未认证"),
  HAS_IDENTIFY( 1,"已认证"),
  ;

  int code;
  String desc;
}