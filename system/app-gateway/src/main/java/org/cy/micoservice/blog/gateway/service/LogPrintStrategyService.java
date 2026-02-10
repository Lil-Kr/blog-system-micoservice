package org.cy.micoservice.blog.gateway.service;

import org.cy.micoservice.blog.entity.gateway.model.entity.LogPrintStrategy;
import org.cy.micoservice.blog.gateway.facade.dto.LogRequestDTO;
import org.cy.micoservice.blog.gateway.facade.print.abst.BaseLogPrintStrategy;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description: 打印日志的策略 service
 */
public interface LogPrintStrategyService {

  /**
   * 获取所有生效的配置
   * @return
   */
  List<LogPrintStrategy> selectPrintStrategyAll();

  /**
   * 初始化阶段加载本地配置到内存中
   */
  void loadProperties();

  /**
   * 获取满足条件的日志打印策略配置集合
   */
  List<BaseLogPrintStrategy> getAvailableStrategy(LogRequestDTO logRequestDTO);

  /**
   * 判断是否有满足条件的日志打印策略配置
   */
  boolean hasAvailableStrategy(LogRequestDTO logRequestDTO);
}