package org.cy.micoservice.blog.gateway.facade.print;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections4.MapUtils;
import org.cy.micoservice.blog.gateway.facade.dto.LogRequestDTO;
import org.cy.micoservice.blog.gateway.facade.print.abst.BaseLogPrintStrategy;

import java.io.Serial;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/11/30
 * @Description: 按照header匹配策略打印日志
 */
public class HeaderNamePrintStrategy extends BaseLogPrintStrategy {

  @Serial
  private static final long serialVersionUID = -9047007067169481187L;

  private String headerName;

  private String headerValue;

  public HeaderNamePrintStrategy(String body) {
    super(body);
  }

  @Override
  public void convertFromBody() {
    HeaderNamePrintStrategy headerNamePrintStrategy = JSON.parseObject(super.getStrategyBody(), HeaderNamePrintStrategy.class);
    this.setHeaderName(headerNamePrintStrategy.getHeaderName());
    this.setHeaderValue(headerNamePrintStrategy.getHeaderValue());
  }

  @Override
  public boolean isSupport(LogRequestDTO logRequestDTO) {
    Map<String, List<String>> headers = logRequestDTO.getHeaders();
    if (MapUtils.isEmpty(headers)) {
      return false;
    }
    return headers.containsKey(headerName);
  }

  public String getHeaderName() {
    return headerName;
  }

  public void setHeaderName(String headerName) {
    this.headerName = headerName;
  }

  public String getHeaderValue() {
    return headerValue;
  }

  public void setHeaderValue(String headerValue) {
    this.headerValue = headerValue;
  }
}
