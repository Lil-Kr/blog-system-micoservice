package org.cy.micoservice.blog.gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.gateway.service.LogPrintStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description: 网关日志打印策略配置类
 */
@Slf4j
@Configuration
public class LogPrintStrategyConfig {

  @Autowired
  private LogPrintStrategyService logPrintStrategyService;

  @PostConstruct
  public void initLogPrintStrategy() {
    logPrintStrategyService.loadProperties();
  }

}