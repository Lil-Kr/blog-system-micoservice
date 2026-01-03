package org.cy.micoservice.blog.gateway.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/30
 * @Description: 网关日志打印策略dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayLogPrintStrategyDTO implements Serializable {
  @Serial
  private static final long serialVersionUID = -2918953904267430954L;

  /**
   * 策略名称
   */
  private String strategyName;

  /**
   * 策略类型
   */
  private String strategyType;

  /**
   * 策略内容
   */
  private String strategyBody;

}
