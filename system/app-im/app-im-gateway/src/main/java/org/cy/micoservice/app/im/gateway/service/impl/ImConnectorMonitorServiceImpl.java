package org.cy.micoservice.app.im.gateway.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.im.facade.contstants.ImMonitorCacheConstant;
import org.cy.micoservice.app.im.gateway.service.ImConnectorMonitorService;
import org.cy.micoservice.app.im.gateway.dto.ImConnectorMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: 获取 IM 连接层配置监控信息
 */
@Slf4j
@Service
public class ImConnectorMonitorServiceImpl implements ImConnectorMonitorService {

  @Autowired
  private RedisTemplate<String, String> stringRedisTemplate;

  private Set<String> connectionNodeAddress = new HashSet<>();

  @Override
  public void refreshCache(Set<String> nodeAddressSet) {
    this.connectionNodeAddress = nodeAddressSet;
  }

  /**
   * 获取下游所有节点的监控配置信息
   * @return
   */
  @Override
  public List<ImConnectorMonitor> getImConnectorMonitorAllList() {
    return this.connectionNodeAddress.stream()
      .map(nodeAddress -> nodeAddress.split(":"))
      .filter(addressArr -> addressArr.length == 2)
      .map(addressArr -> {
        /**
         * addressArr[0]: ip
         * addressArr[1]: port
         */
        String cacheKey = String.format(ImMonitorCacheConstant.IM_CONNECTOR_MONITOR_KEY, addressArr[0], addressArr[1]);
        Object connectionObj = stringRedisTemplate.opsForHash().get(cacheKey, ImMonitorCacheConstant.IM_CONNECTOR_CONNECTION_KEY);
        log.info("cacheKey: {}, connectionsObj: {}", cacheKey, connectionObj);
        ImConnectorMonitor imConnectorMonitor = ImConnectorMonitor.builder()
          .ip(addressArr[0])
          .port(Integer.parseInt(addressArr[1]))
          .build();

        if (connectionObj == null) {
          // 服务初始化, 没有连接的情况下, 无缓存
          imConnectorMonitor.setConnections(0);
        } else {
          imConnectorMonitor.setConnections(Integer.parseInt(String.valueOf(connectionObj)));
        }
        return imConnectorMonitor;
      })
      .collect(Collectors.toList());
  }
}
