package org.cy.micoservice.blog.gateway.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum LogPrintStrategyTypeEnum {

  BASE_TIME_GAP("base_time_gap","基于时间区间段"),
  BASE_PATH("base_path","基于请求路径"),
  BASE_SERVICE_NAME("base_service_name","基于请求服务名"),
  BASE_HEADER("base_header","基于请求header"),
  BASE_USER_ID("base_user_id","基于用户id识别"),
  BASE_TIME_COST("base_time_cost","基于时间耗时"),
  BASE_RESPONSE_ERROR_CODE("base_response_error_code","基于响应异常记录");

  String code;
  String desc;

  public static LogPrintStrategyTypeEnum getByCode(String code) {
    return Arrays.stream(LogPrintStrategyTypeEnum.values()).filter(item -> item.getCode().equals(code)).findAny().orElse(null);
  }

  public static String getDesc(String code) {
    LogPrintStrategyTypeEnum typeEnum = getByCode(code);
    return typeEnum != null ? typeEnum.getDesc() : null;
  }
}