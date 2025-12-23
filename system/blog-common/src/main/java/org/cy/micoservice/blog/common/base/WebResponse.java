package org.cy.micoservice.blog.common.base;

import lombok.Data;
import org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.blog.common.enums.response.RpcReturnCodeEnum;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * @Author: Lil-K
 * @Date: 2025/11/26
 * @Description:
 */
@Data
public class WebResponse<T> implements Serializable {
  @Serial
  private static final long serialVersionUID = 8163283436814592499L;

  //响应code
  private Integer code;
  //响应消息code描述文案
  private String msg;
  //数据内容
  private T data;

  private String traceId;

  public WebResponse() {
    this.traceId =  UUID.randomUUID().toString();
  }

  public WebResponse(int code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
    this.traceId = UUID.randomUUID().toString();
  }

  public WebResponse(ApiReturnCodeEnum codeEnum) {
    this.code = codeEnum.getCode();
    this.msg = codeEnum.getMessage();
    this.data = null;
    this.traceId = UUID.randomUUID().toString();
  }

  private static <T> WebResponse<T> create(int code, String msg, T data) {
    return new WebResponse<>(code, msg, data);
  }

  public static <T> WebResponse<T> success() {
    return create(ApiReturnCodeEnum.HTTP_SUCCESS.getCode(), ApiReturnCodeEnum.HTTP_SUCCESS.getMessage(), null);
  }

  public static <T> WebResponse<T> success(T data) {
    return create(ApiReturnCodeEnum.HTTP_SUCCESS.getCode(), ApiReturnCodeEnum.HTTP_SUCCESS.getMessage(), data);
  }

  public static <T> WebResponse<T> busError() {
    return create(ApiReturnCodeEnum.BUSINESS_ERROR.getCode(), ApiReturnCodeEnum.BUSINESS_ERROR.getMessage(), null);
  }

  public static <T> WebResponse<T> busError(ApiReturnCodeEnum apiReturnCodeEnum) {
    return create(apiReturnCodeEnum.getCode(), apiReturnCodeEnum.getMessage(), null);
  }

  public static <T> WebResponse<T> busError(int code, String msg) {
    return create(code, msg, null);
  }

  public static <T> WebResponse<T> unknowError() {
    return create(ApiReturnCodeEnum.SYSTEM_ERROR.getCode(), ApiReturnCodeEnum.SYSTEM_ERROR.getMessage(), null);
  }

  public static <T> WebResponse<T> sysError(String msg) {
    return create(ApiReturnCodeEnum.SYSTEM_ERROR.getCode(), ApiReturnCodeEnum.SYSTEM_ERROR.getMessage(), null);
  }

  public static <T> WebResponse<T> errorParam() {
    return create(RpcReturnCodeEnum.RPC_PARAMETER_ERROR.getCode(), RpcReturnCodeEnum.RPC_PARAMETER_ERROR.getMessage(), null);
  }
}