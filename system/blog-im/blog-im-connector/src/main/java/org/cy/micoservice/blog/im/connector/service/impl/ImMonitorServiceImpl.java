package org.cy.micoservice.blog.im.connector.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.cy.micoservice.blog.im.connector.config.ImConnectorProperties;
import org.cy.micoservice.blog.im.connector.service.ImMonitorService;
import org.cy.micoservice.blog.im.facade.connector.contstants.ImMonitorCacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: im连接层埋点 service实现
 */
@Slf4j
@Service
public class ImMonitorServiceImpl implements ImMonitorService {

  @Autowired
  private RedisTemplate<String, String> stringRedisTemplate;
  @Autowired
  private ImConnectorProperties imConnectorProperties;

  @Override
  public void initConnection() {
    try {
      String cacheKey = String.format(ImMonitorCacheConstant.IM_CONNECTOR_MONITOR_KEY, NetUtils.getLocalHost(), imConnectorProperties.getWsPort());
      stringRedisTemplate.opsForHash().put(cacheKey, ImMonitorCacheConstant.IM_CONNECTOR_CONNECTION_KEY, String.valueOf(0));
      /**
       * 7天没有连接进来, 自动失效
       */
      stringRedisTemplate.expire(cacheKey, 7, TimeUnit.DAYS);
    } catch (Exception e) {
      log.error("initConnection error", e);
    }
  }

  @Override
  public void incrConnection() {
    try {
      String cacheKey = String.format(ImMonitorCacheConstant.IM_CONNECTOR_MONITOR_KEY, NetUtils.getLocalHost(), imConnectorProperties.getWsPort());
      stringRedisTemplate.opsForHash().increment(cacheKey, ImMonitorCacheConstant.IM_CONNECTOR_CONNECTION_KEY, 1);
      stringRedisTemplate.expire(cacheKey, 7, TimeUnit.DAYS);
    } catch (Exception e) {
      log.error("incrConnection error: ", e);
    }
  }

  @Override
  public void decrConnection() {
    try {
      String cacheKey = String.format(ImMonitorCacheConstant.IM_CONNECTOR_MONITOR_KEY, NetUtils.getLocalHost(), imConnectorProperties.getWsPort());
      stringRedisTemplate.opsForHash().increment(cacheKey, ImMonitorCacheConstant.IM_CONNECTOR_CONNECTION_KEY, -1);
      /**
       * 7天没有连接进来, 自动失效
       */
      stringRedisTemplate.expire(cacheKey, 7, TimeUnit.DAYS);
    } catch (Exception e) {
      log.error("decrConnection error: ", e);
    }
  }
}
