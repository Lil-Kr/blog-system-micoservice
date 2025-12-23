package org.cy.micoservice.blog.infra.console.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;
import org.cy.micoservice.blog.infra.console.dao.RouteChangeLogMapper;
import org.cy.micoservice.blog.infra.console.service.RouteConfigChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
@Service
public class RouteConfigChangeLogServiceImpl implements RouteConfigChangeLogService {

  @Autowired
  private RouteChangeLogMapper routeChangeLogMapper;

  @Override
  public List<RouteChangeLog> findGtVersion(Long version) {
    return null;
  }

  @Override
  public Integer create(RouteChangeLog routeChangeLog) {
    return routeChangeLogMapper.insert(routeChangeLog);
  }

  @Override
  public Integer edit(RouteChangeLog routeChangeLog) {
    return routeChangeLogMapper.updateById(routeChangeLog);
  }

  @Override
  public Integer deleteById(RouteChangeLog routeChangeLog) {
    return routeChangeLogMapper.deleteById(routeChangeLog);
  }

  @Override
  public List<RouteChangeLog> selectById(Long configId) {
    QueryWrapper<RouteChangeLog> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("config_id", configId);
    return routeChangeLogMapper.selectList(queryWrapper);
  }
}
