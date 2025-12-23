package org.cy.micoservice.blog.gateway.facade.utils;

import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.blog.common.exception.BizException;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description: 解析 Nacos 配置版本,  lastUpdateVersion=16
 */
public class NacosRouteVersionUtils {

  private static final String CONFIG_KEY_NAME = "lastUpdateVersion";

  public static Long parseConfigVersionFromConfig(String config) {
    if (StringUtils.isBlank(config) || StringUtils.containsNone(config, "=")) {
      throw new BizException(ApiReturnCodeEnum.GATEWAY_STARTING_ERROR);
    }
    String[] configs = config.split("=");
    if (configs.length != 2) {
      throw new BizException("invalid nacos config length", ApiReturnCodeEnum.GATEWAY_STARTING_ERROR);
    }
    String name = configs[0];
    if (! CONFIG_KEY_NAME.equals(name)) {
      throw new BizException("invalid nacos config key", ApiReturnCodeEnum.GATEWAY_STARTING_ERROR);
    }
    String version = configs[1];
    return Long.valueOf(version);
  }

  public static String formatConfigVersion(long version) {
    return CONFIG_KEY_NAME + "=" + version;
  }
}
