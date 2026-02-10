package org.cy.micoservice.app.common.enums.exception;

import lombok.AllArgsConstructor;
import org.cy.micoservice.app.common.enums.BaseEnum;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description:
 */
@AllArgsConstructor
public enum BizErrorEnum implements BaseEnum {

  PARAM_ERROR(2000, "参数异常"),
  PHONE_REGISTRY(2001, "手机号已经被注册使用"),
  CAPTCHA_CHECK_INVALID(2002, "验证凭证异常"),
  SMS_LOGIN_CODE_ERROR(2003, "验证码错误"),
  SMS_LOGIN_CODE_EXPIRED(2004, "验证码已过期"),
  INVALID_FILE_TYPE(2005, "非法文件格式类型"),
  TOKEN_EXPIRED(3000, "凭证过期"),
  TOKEN_ILLEGAL(3001, "凭证非法"),
  FOLLOW_SELF_ERROR(3002, "请勿关注自己"),
  SMS_SEND_ERROR(3003, "短信发送异常"),
  SYSTEM_ERROR(5000, "系统异常")
    ;

  private final Integer errorCode;

  private final String errorMsg;

  @Override
  public Integer getErrorCode() {
    return errorCode;
  }

  @Override
  public String getErrorMsg() {
    return errorMsg;
  }
}
