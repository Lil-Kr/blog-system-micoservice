package org.cy.micoservice.app.common.enums.response;

import org.cy.micoservice.app.common.enums.BaseEnum;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description: RpcReturnCodeEnum
 */
public enum RpcReturnCodeEnum implements BaseEnum {

  RPC_SUCCESS(200, "RPC call success"),
  RPC_PARAMETER_ERROR(500, "RPC error param"),
  RPC_UNKNOW_ERROR_MSG(500, "unknow system param"),
  RPC_REQUEST_ERROR(500, "RPC request fail")
  ;

  private Integer code;

  private String message;

  RpcReturnCodeEnum(Integer code, String desc) {
    this.code = code;
    this.message = desc;
  }

  @Override
  public Integer getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public Integer getErrorCode() {
    return code;
  }

  @Override
  public String getErrorMsg() {
    return message;
  }
}
