package org.cy.micoservice.blog.im.facade.contstants;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description:
 */
public class ImMonitorCacheConstant {

  /**
   * im-connector 监控信息缓存key
   * service-name:[ip]:[port]
   */
  public static final String IM_CONNECTOR_MONITOR_KEY = "blog-im-connector:%s:%s";

  public static final String IM_CONNECTOR_CONNECTION_KEY = "connections";

  public static final String IM_CONNECTOR_ADDRESS_KEY = "%s%s:%s";
}