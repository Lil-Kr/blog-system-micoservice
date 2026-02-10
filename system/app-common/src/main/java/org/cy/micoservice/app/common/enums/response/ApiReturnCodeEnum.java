package org.cy.micoservice.app.common.enums.response;

import org.cy.micoservice.app.common.enums.BaseEnum;

/**
 * @Author: Lil-K
 * @Date: 2025/6/8
 * @Description: ApiReturnCodeEnum
 */
public enum ApiReturnCodeEnum implements BaseEnum {

  SUCCESS(200, "操作成功"),
  REQUEST_SUCCESS(200, "请求成功"),
  HTTP_SUCCESS(200, "HTTP 调用成功"),
  WARNING(401, "系统警告"),
  REQUEST_ERROR(404, "资源不存在"),
  USER_INFO_NO_EXIST(411, "用户不存在"),
  USER_INFO_ERROR(412, "用户名或密码错误"),
  INFO_NOT_EXIST(413, "信息不存在, 操作失败"),
  INFO_EXIST(414, "信息已存在, 操作失败"),
  USER_OLD_PASSWORD_ERROR(415, "原密码错误"),
  SAME_PASSWORD_ERROR(416, "不能与原密码一样"),
  SEND_MAIL_ERROR(417, "调用邮件服务器错误"),
  DATA_INFO_REPEAT(418, "数据重复, 请求失败"),
  DATA_INFO_UNUSUAL(419, "数据异常或操作不规范"),
  ADD_ERROR(421, "添加失败"),
  UPDATE_ERROR(422, "更新失败"),
  DEL_ERROR(423, "删除失败"),
  OPERATE_ERROR(424, "请求失败"),
  PARAMETER_ERROR(426, "参数错误"),
  SYSTEM_ERROR(500, "系统异常"),
  BUSINESS_ERROR(501, "业务异常"),
  SECRET_ERROR(502, "密钥错误"),
  MAX_LIMIT(503, "登录失败次数超过上线阈值"),
  LOGIN_ACCOUNT_ERROR(504, "邮箱或密码错误"),
  ILLEGAL_CHARACTERS_ERROR(505, "参数中含有非法字符"),
  TOO_MANY_REQUEST(506, "too many request"),
  NO_ACCESS(510, "无权访问"),
  GATEWAY_STARTING_ERROR(601, "Gateway启动异常"),
  GATEWAY_ROUTE_ERROR(508, "网关路由转发异常"),
  DO_NOT_INJECT(4003, "禁止注入扫描"),
  NOT_LOGIN(4004, "请重新登录!"),
  ;

  private Integer code;

  private String message;

  ApiReturnCodeEnum(Integer code, String desc) {
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
