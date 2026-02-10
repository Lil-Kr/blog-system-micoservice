package org.cy.micoservice.app.common.exception;

import org.cy.micoservice.app.common.exception.interfacese.BaseError;
import org.cy.micoservice.app.common.enums.response.ApiReturnCodeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description: RPC 异常
 */

public class RpcException extends RuntimeException implements BaseError, Serializable {

  @Serial
  private static final long serialVersionUID = 6085645590759675632L;

  private ApiReturnCodeEnum apiReturnCodeEnum;

  private Integer errorCode;

  private String errorMsg;

  public RpcException(String message) {
    super(message);
  }

  public RpcException(BaseError baseError) {
    super(baseError.getErrorMsg());
    this.errorCode = baseError.getErrorCode();
    this.errorMsg = baseError.getErrorMsg();
  }

  public RpcException(ApiReturnCodeEnum apiReturnCodeEnum) {
    super(apiReturnCodeEnum.getMessage());
    this.apiReturnCodeEnum = apiReturnCodeEnum;
  }

  public RpcException(String message, ApiReturnCodeEnum apiReturnCodeEnum) {
    super(message);
    this.apiReturnCodeEnum = apiReturnCodeEnum;
  }

  public ApiReturnCodeEnum getReturnCodeEnum() {
    return apiReturnCodeEnum;
  }

  @Override
  public Integer getErrorCode() {
    return apiReturnCodeEnum.getCode();
  }

  @Override
  public String getErrorMsg() {
    return apiReturnCodeEnum.getMessage();
  }
}