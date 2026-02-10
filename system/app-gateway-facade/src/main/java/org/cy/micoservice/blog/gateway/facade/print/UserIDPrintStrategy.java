package org.cy.micoservice.blog.gateway.facade.print;


import com.alibaba.fastjson2.JSON;
import org.cy.micoservice.blog.gateway.facade.dto.LogRequestDTO;
import org.cy.micoservice.blog.gateway.facade.print.abst.BaseLogPrintStrategy;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/11
 * @Description: 基于jwt身份识别
 */
public class UserIDPrintStrategy extends BaseLogPrintStrategy {

  @Serial
  private static final long serialVersionUID = 2606990175098712079L;

  private Long userId;

  public UserIDPrintStrategy(String body) {
    super(body);
  }

  @Override
  public void convertFromBody() {
    UserIDPrintStrategy headerNamePrintStrategy = JSON.parseObject(super.getStrategyBody(), UserIDPrintStrategy.class);
    this.setUserId(headerNamePrintStrategy.getUserId());
  }

  @Override
  public boolean isSupport(LogRequestDTO logRequestDTO) {
    return logRequestDTO.getUserId() != null && logRequestDTO.getUserId().equals(userId);
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
