package org.cy.micoservice.blog.gateway.facade.constants;

import lombok.Data;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: 网关常量管理配置
 */
public class GatewayConstants {

  public static final String AUTH_HEADER_NAME = "auth_header_token";

  @Data
  public static class GatewayAttrKey {
    public static String X_ROUTE = "x-route";
    public static String X_ROUTE_ID = "x-route-id";
    public static String X_ROUTE_ERROR_CODE = "x-route-id-code";
    public static String X_ROUTE_ERROR_MSG = "x-route-id-msg";
    public static String X_REQUEST_BODY = "x-route-body";
    public static String X_AUTHORIZATION = "x-authorization";
    public static String X_JWT_INFO = "x-jwt-info";
    public static String X_USERID = "x-userid";
  }

  @Data
  public static class GatewayOrder {
    // public static int REQUEST_LOG_FILTER_ORDER = 99;
    public static int HTTP_REQUEST_PATH_VALID_FILTER_ORDER = 100;
    public static int HTTP_PARAMETER_RESOLVER_FILTER_ORDER = 102;
    public static int AUTH_FILTER_ORDER = 105;
    public static int LOG_PRINT_STRATEGY_FILTER_ORDER = 106;
    public static int RATE_LIMIT_ORDER = 108;
    public static int EXCEPTION_HANDLER_FILTER_ORDER = 109;
    public static int DUBBO_INVOKE_FILTER_ORDER = 110;
  }
}