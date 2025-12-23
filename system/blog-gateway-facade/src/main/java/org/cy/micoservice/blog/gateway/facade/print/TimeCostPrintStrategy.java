package org.cy.micoservice.blog.gateway.facade.print;


import com.alibaba.fastjson2.JSON;
import org.cy.micoservice.blog.gateway.facade.dto.LogRequestDTO;
import org.cy.micoservice.blog.gateway.facade.print.abst.BaseLogPrintStrategy;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/11
 * @Description: 基于时间耗时打印策略
 */
public class TimeCostPrintStrategy extends BaseLogPrintStrategy {

  @Serial
  private static final long serialVersionUID = -1529749027419998626L;

  private Long timeCost;

  public TimeCostPrintStrategy(String body) {
    super(body);
  }

  @Override
  public void convertFromBody() {
    TimeCostPrintStrategy headerNamePrintStrategy = JSON.parseObject(super.getStrategyBody(), TimeCostPrintStrategy.class);
    this.setTimeCost(headerNamePrintStrategy.getTimeCost());
  }

  @Override
  public boolean isSupport(LogRequestDTO logRequestDTO) {
    return logRequestDTO.getTimeCost() != null && logRequestDTO.getTimeCost() > timeCost;
  }

  public Long getTimeCost() {
    return timeCost;
  }

  public void setTimeCost(Long timeCost) {
    this.timeCost = timeCost;
  }
}
