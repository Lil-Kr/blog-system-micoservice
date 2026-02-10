package org.cy.micoservice.blog.common.exception;


import org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.blog.common.exception.interfacese.BaseError;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2024/3/14
 * @Description: 业务异常处理
 */
public class BizException extends RuntimeException implements BaseError, Serializable {

  @Serial
  private static final long serialVersionUID = 4812984750725110363L;

  private ApiReturnCodeEnum apiReturnCodeEnum;

  private Integer errorCode;

  private String errorMsg;

  public BizException(String message) {
    super(message);
    this.errorMsg = message;
  }

  public BizException(BaseError baseError) {
    super(baseError.getErrorMsg());
    this.errorCode = baseError.getErrorCode();
    this.errorMsg = baseError.getErrorMsg();
  }

  public BizException(ApiReturnCodeEnum apiReturnCodeEnum) {
    super(apiReturnCodeEnum.getMessage());
    this.apiReturnCodeEnum = apiReturnCodeEnum;
  }

  public BizException(String message, ApiReturnCodeEnum apiReturnCodeEnum) {
    super(message);
    this.apiReturnCodeEnum = apiReturnCodeEnum;
  }

  public BizException(Integer code, String msg) {
    super(msg);
    this.errorCode = code;
    this.errorMsg = msg;
  }

  public ApiReturnCodeEnum getReturnCodeEnum() {
    return apiReturnCodeEnum;
  }

  @Override
  public Integer getErrorCode() {
    if (apiReturnCodeEnum != null) {
      return apiReturnCodeEnum.getCode();
    }
    return errorCode;
  }

  @Override
  public String getErrorMsg() {
    if (apiReturnCodeEnum != null) {
      return apiReturnCodeEnum.getMessage();
    }
    return errorMsg;
  }
}
