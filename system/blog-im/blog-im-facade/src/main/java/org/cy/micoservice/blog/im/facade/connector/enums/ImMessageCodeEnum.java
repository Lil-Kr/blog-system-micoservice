package org.cy.micoservice.blog.im.facade.connector.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cy.micoservice.blog.im.facade.connector.contstants.ImMessageConstants;

import java.util.Arrays;

/**
 * @Author: Lil-K
 * @Date: 2025/12/10
 * @Description: im消息体code枚举
 */
@Getter
@AllArgsConstructor
public enum ImMessageCodeEnum {

  LOGIN(ImMessageConstants.LOGIN_MSG_CODE, "login"),
  LOGOUT(ImMessageConstants.LOGOUT_MSG_CODE, "logout"),
  SHAKE_HAND(ImMessageConstants.SHAKE_HAND_MSG_CODE, "shake hand message"),
  BIZ(ImMessageConstants.BIZ_MSG_CODE, "biz message"),
  HEART_BEAT_MSG(ImMessageConstants.HEART_BEAT_MSG_CODE, "heart beat message"),
    ;

  int code;
  String msg;

  public static ImMessageCodeEnum getByCode(int code) {
    return Arrays.stream(ImMessageCodeEnum.values()).filter(e -> e.getCode() == code).findFirst().orElse(null);
  }
}