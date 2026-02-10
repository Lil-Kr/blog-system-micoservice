package org.cy.micoservice.app.gateway.facade.print.abst;

import lombok.Data;
import org.cy.micoservice.app.gateway.facade.dto.LogRequestDTO;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/30
 * @Description: 基础日志打印策略
 */
@Data
public abstract class BaseLogPrintStrategy implements Serializable {

  @Serial
  private static final long serialVersionUID = -8002331236033287441L;

  public BaseLogPrintStrategy(String body) {
    this.strategyBody = body;
  }

  /**
   * 策略配置内容 body json 格式的对象
   */
  private String strategyBody;

  /**
   * 转换过程
   */
  public abstract void convertFromBody();

//  public abstract String getLogPrintBody();

  /**
   * 判断当前策略是否满足
   */
  public abstract boolean isSupport(LogRequestDTO logRequestDTO);

}
