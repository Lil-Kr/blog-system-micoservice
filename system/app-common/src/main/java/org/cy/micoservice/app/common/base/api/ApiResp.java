package org.cy.micoservice.app.common.base.api;

import lombok.Data;
import org.cy.micoservice.app.common.enums.response.ApiReturnCodeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/6/8
 * @Description: API response body
 */
@Data
public final class ApiResp<T> implements Serializable {

  @Serial
  private static final long serialVersionUID = 7609876341720647772L;

  public static final String MSG_RENEWAL_SUCCESS = "renewal success";

  public static final String MSG_SUCCESS = "SUCCESS";

  public static final String MSG_ERROR = "ERROR";

  public static final String MSG_FAILURE = "FAILURE";

  /**响应码*/
  private Integer code;

  /**响应信息**/
  private String msg;

  /**响应数据**/
  private T data;

  public ApiResp() {
  }

  public ApiResp(int code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  /**
   * create response body
   * @param code
   * @param msg
   * @param data
   * @return
   * @param <T>
   */
  private static <T> ApiResp<T> create(int code, String msg, T data) {
    return new ApiResp<>(code, msg, data);
  }

  /**
   * 成功
   * @param msg
   * @return
   */
  public static <T> ApiResp<T> success(String msg) {
    return create(ApiReturnCodeEnum.REQUEST_SUCCESS.getCode(), msg,null);
  }

  /**
   * 成功
   * @return
   * @param <T>
   */
  public static <T> ApiResp<T> success() {
    return create(ApiReturnCodeEnum.REQUEST_SUCCESS.getCode(), ApiReturnCodeEnum.REQUEST_SUCCESS.getMessage(),null);
  }

  /**
   * 成功
   * @return
   * @param <T>
   */
  public static <T> ApiResp<T> success(ApiReturnCodeEnum apiReturnCodeEnum) {
    return create(apiReturnCodeEnum.getCode(), apiReturnCodeEnum.getMessage(),null);
  }

  /**
   * 成功
   * @param data
   * @return
   */
  public static <T> ApiResp<T> success(T data) {
    return create(ApiReturnCodeEnum.REQUEST_SUCCESS.getCode(), ApiReturnCodeEnum.REQUEST_SUCCESS.getMessage(), data);
  }

  /**
   * 成功
   * @param msg
   * @param data
   * @return
   */
  public static <T> ApiResp<T> success(String msg, T data) {
    return create(ApiReturnCodeEnum.REQUEST_SUCCESS.getCode(), msg, data);
  }

  /**
   * 失败
   * @return
   */
  public static <T> ApiResp<T> failure() {
    return create(ApiReturnCodeEnum.SYSTEM_ERROR.getCode(), ApiReturnCodeEnum.SYSTEM_ERROR.getMessage(),null);
  }

  /**
   * 失败
   * @param msg
   * @return
   */
  public static <T> ApiResp<T> failure(String msg) {
    return create(ApiReturnCodeEnum.SYSTEM_ERROR.getCode(), msg,null);
  }

  /**
   * 失败
   * @param msg
   * @return
   */
  public static <T> ApiResp<T> failure(int code, String msg) {
    return create(code, msg,null);
  }

  /**
   * @param apiReturnCodeEnum
   * @return
   * @param <T>
   */
  public static <T> ApiResp<T> failure(ApiReturnCodeEnum apiReturnCodeEnum) {
    return create(apiReturnCodeEnum.getCode(), apiReturnCodeEnum.getMessage(),null);
  }

  /**
   *
   * @return
   * @param <T>
   */
  public static <T> ApiResp<T> failure(T data) {
    return create(ApiReturnCodeEnum.SYSTEM_ERROR.getCode(), ApiReturnCodeEnum.SYSTEM_ERROR.getMessage(),data);
  }

  /**
   * 警告
   * @param code
   * @param msg
   * @return
   * @param <T>
   */
  public static <T> ApiResp<T> warning(int code, String msg) {
    return create(code, msg, null);
  }

  /**
   * 警告
   * @param code
   * @param msg
   * @param data
   * @return
   * @param <T>
   */
  public static <T> ApiResp<T> warning(int code, String msg, T data) {
    return create(code, msg, data);
  }

  /**
   * 警告
   * @param msg
   * @return
   */
  public static <T> ApiResp<T> warning(String msg) {
    return create(ApiReturnCodeEnum.WARNING.getCode(), msg, null);
  }

  /**
   * 警告
   * @param data
   * @return
   */
  public static <T> ApiResp<T> warning(T data) {
    return create(ApiReturnCodeEnum.WARNING.getCode(), ApiReturnCodeEnum.WARNING.getMessage(), data);
  }

  /**
   * 警告
   * @return
   */
  public static <T> ApiResp<T> warning() {
    return create(ApiReturnCodeEnum.SYSTEM_ERROR.getCode(), ApiReturnCodeEnum.SYSTEM_ERROR.getMessage(), null);
  }

  /**
   *
   * @param apiReturnCodeEnum
   * @return
   * @param <T>
   */
  public static <T> ApiResp<T> warning(ApiReturnCodeEnum apiReturnCodeEnum) {
    return create(apiReturnCodeEnum.getCode(), apiReturnCodeEnum.getMessage(),null);
  }
}
