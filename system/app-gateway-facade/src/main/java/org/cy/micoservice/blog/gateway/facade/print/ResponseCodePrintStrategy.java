package org.cy.micoservice.blog.gateway.facade.print;


import com.alibaba.fastjson2.JSON;
import org.cy.micoservice.blog.gateway.facade.dto.LogRequestDTO;
import org.cy.micoservice.blog.gateway.facade.print.abst.BaseLogPrintStrategy;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/11
 * @Description: 基于响应状态码打印策略
 */
public class ResponseCodePrintStrategy extends BaseLogPrintStrategy {

  @Serial
  private static final long serialVersionUID = -6241768895292535099L;

  private String code;

  public ResponseCodePrintStrategy(String body) {
    super(body);
  }

  @Override
  public void convertFromBody() {
    ResponseCodePrintStrategy responseCodePrintStrategy = JSON.parseObject(super.getStrategyBody(), ResponseCodePrintStrategy.class);
    this.setCode(responseCodePrintStrategy.getCode());
  }

  @Override
  public boolean isSupport(LogRequestDTO logRequestDTO) {
    return logRequestDTO.getResponseCode() != null && logRequestDTO.getResponseCode().equals(this.getCode());
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
