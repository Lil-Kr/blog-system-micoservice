package org.cy.micoservice.app.gateway.facade.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/11/30
 * @Description: 标准化日志请求体DTO
 */
@Data
public class LogRequestDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = -3354160681365295477L;

  /**
   * 事件时间
   */
  private Long eventTime;

  /**
   * 请求路径
   */
  private String path;

  /**
   * 下游转发服务
   */
  private String serviceName;

  /**
   * http请求header内容
   */
  private Map<String, List<String>> headers;

  /**
   * jwt内部的userId值
   */
  private Long userId;

  /**
   * 响应码
   */
  private String responseCode;

  /**
   * 耗时
   */
  private Long timeCost;
}