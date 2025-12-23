package org.cy.micoservice.blog.im.gateway.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: im-connector层的连接负载信息
 */
@Data
@Builder
public class ImConnectorMonitorInfo {

  /**
   * im机器 ip 地址
   */
  private String ip;

  /**
   * ws协议端口
   */
  private int port;

  /**
   * 连接数
   */
  private int connections;
}