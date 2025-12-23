package org.cy.micoservice.blog.gateway.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.entity.gateway.model.entity.LogPrintStrategy;
import org.cy.micoservice.blog.entity.gateway.model.enums.LogPrintStrategyTypeEnum;
import org.cy.micoservice.blog.gateway.dao.LogPrintStrategyMapper;
import org.cy.micoservice.blog.gateway.facade.dto.LogRequestDTO;
import org.cy.micoservice.blog.gateway.facade.print.*;
import org.cy.micoservice.blog.gateway.facade.print.abst.BaseLogPrintStrategy;
import org.cy.micoservice.blog.gateway.service.LogPrintStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Lil-K
 * @Date: 2025/12/1
 * @Description: 网关打印策略
 */
@Service
@Slf4j
public class LogPrintStrategyServiceImpl implements LogPrintStrategyService {

  private List<BaseLogPrintStrategy> logPrintStrategiesCache = new CopyOnWriteArrayList<>();

  public List<BaseLogPrintStrategy> getLogPrintStrategiesCache() {
    return this.logPrintStrategiesCache;
  }

  @Autowired
  private LogPrintStrategyMapper logPrintStrategyMapper;

  @Override
  public List<LogPrintStrategy> selectPrintStrategyAll() {
    QueryWrapper<LogPrintStrategy> qw = new QueryWrapper<>();
    qw.eq("deleted", DeleteStatusEnum.ACTIVE.getCode());
    return logPrintStrategyMapper.selectList(qw);
  }

  @Override
  public void loadProperties() {
    List<BaseLogPrintStrategy> baseLogPrintStrategies = new CopyOnWriteArrayList<>();

    // 网关所有生效的打印日志策略
    List<LogPrintStrategy> logPrintStrategies = this.selectPrintStrategyAll();

    for (LogPrintStrategy logPrintStrategy : logPrintStrategies) {
      BaseLogPrintStrategy convertFrom = this.convertFrom(logPrintStrategy);
      if (Objects.nonNull(convertFrom)) {
        baseLogPrintStrategies.add(convertFrom);
      }
    }
    this.logPrintStrategiesCache = baseLogPrintStrategies;
    log.info("load log print strategy properties success, strategy info: {}", JSONArray.toJSONString(logPrintStrategies));
  }

  @Override
  public List<BaseLogPrintStrategy> getAvailableStrategy(LogRequestDTO logRequestDTO) {
    return this.getLogPrintStrategiesCache().stream().filter(s -> s.isSupport(logRequestDTO)).toList();
  }

  @Override
  public boolean hasAvailableStrategy(LogRequestDTO logRequestDTO) {
    return this.getLogPrintStrategiesCache().stream().anyMatch(item -> item.isSupport(logRequestDTO));
  }

  private BaseLogPrintStrategy convertFrom(LogPrintStrategy logPrintStrategy) {
    LogPrintStrategyTypeEnum type = LogPrintStrategyTypeEnum.getByCode(logPrintStrategy.getStrategyType());
    if (type == null) return null;
    String strategyBody = logPrintStrategy.getStrategyBody();
    BaseLogPrintStrategy baseLogPrintStrategy = null;
    switch (type) {
      case BASE_HEADER -> {
        baseLogPrintStrategy = new HeaderNamePrintStrategy(strategyBody);
        break;
      }
      case BASE_PATH -> {
        baseLogPrintStrategy = new PathMappingPrintStrategy(strategyBody);
        break;
      }
      case BASE_SERVICE_NAME -> {
        baseLogPrintStrategy = new ServiceNamePrintStrategy(strategyBody);
        break;
      }
      case BASE_TIME_GAP -> {
        baseLogPrintStrategy = new TimeGapPrintStrategy(strategyBody);
        break;
      }
      case BASE_USER_ID -> {
        baseLogPrintStrategy = new UserIDPrintStrategy(strategyBody);
        break;
      }
      case BASE_TIME_COST -> {
        baseLogPrintStrategy = new TimeCostPrintStrategy(strategyBody);
        break;
      }
      case BASE_RESPONSE_ERROR_CODE -> {
        baseLogPrintStrategy = new ResponseCodePrintStrategy(strategyBody);
        break;
      }
    }

    if (baseLogPrintStrategy == null) return null;

    baseLogPrintStrategy.convertFromBody();
    return baseLogPrintStrategy;
  }
}
