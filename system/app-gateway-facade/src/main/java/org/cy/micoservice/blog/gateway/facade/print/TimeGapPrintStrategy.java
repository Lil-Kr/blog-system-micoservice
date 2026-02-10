package org.cy.micoservice.blog.gateway.facade.print;


import com.alibaba.fastjson2.JSON;
import org.cy.micoservice.blog.gateway.facade.dto.LogRequestDTO;
import org.cy.micoservice.blog.gateway.facade.print.abst.BaseLogPrintStrategy;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/11
 * @Description: 基于时间间隔的日志打印策略
 */
public class TimeGapPrintStrategy extends BaseLogPrintStrategy {

  @Serial
  private static final long serialVersionUID = 1698593279963341789L;

  private Long startTime;

  private Long endTime;

  public TimeGapPrintStrategy(String body) {
    super(body);
  }

  @Override
  public void convertFromBody() {
    TimeGapPrintStrategy gapPrintStrategy = JSON.parseObject(super.getStrategyBody(), TimeGapPrintStrategy.class);
    this.setStartTime(gapPrintStrategy.getStartTime());
    this.setEndTime(gapPrintStrategy.getEndTime());
  }

  @Override
  public boolean isSupport(LogRequestDTO logRequestDTO) {
    Long currentTime = logRequestDTO.getEventTime();
    if (currentTime == null) {
      return false;
    }
    return startTime <= currentTime && endTime >= currentTime;
  }

  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public Long getEndTime() {
    return endTime;
  }

  public void setEndTime(Long endTime) {
    this.endTime = endTime;
  }

}
