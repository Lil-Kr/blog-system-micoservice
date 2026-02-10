package org.cy.micoservice.blog.gateway.facade.print;


import com.alibaba.fastjson2.JSON;
import org.cy.micoservice.blog.gateway.facade.dto.LogRequestDTO;
import org.cy.micoservice.blog.gateway.facade.print.abst.BaseLogPrintStrategy;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/11
 * @Description: 服务名转发打印策略
 */
public class ServiceNamePrintStrategy extends BaseLogPrintStrategy {

  @Serial
  private static final long serialVersionUID = -7335117186819187564L;

  private String serviceName;

  public ServiceNamePrintStrategy(String body) {
    super(body);
  }

  @Override
  public void convertFromBody() {
    ServiceNamePrintStrategy serviceNamePrintStrategy = JSON.parseObject(super.getStrategyBody(), ServiceNamePrintStrategy.class);
    this.setServiceName(serviceNamePrintStrategy.getServiceName());
  }

  @Override
  public boolean isSupport(LogRequestDTO logRequestDTO) {
    return logRequestDTO.getServiceName() != null && logRequestDTO.getServiceName().equals(this.getServiceName());
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
}
