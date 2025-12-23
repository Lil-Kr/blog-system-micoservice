package org.cy.micoservice.blog.im.gateway.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.im.facade.router.connector.contstants.ImMonitorCacheConstant;
import org.cy.micoservice.blog.im.gateway.dto.ImConnectorMonitorInfo;
import org.cy.micoservice.blog.im.gateway.service.ImConnectorMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: 存储下游存活连接节点信息
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

  @Override
  public List<ImConnectorMonitorInfo> getAll() {
    List<ImConnectorMonitorInfo> imConnectorMonitorInfoList = new ArrayList<>();
    return connectionNodeAddress.stream()
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
        ImConnectorMonitorInfo imConnectorMonitorInfo = ImConnectorMonitorInfo.builder()
          .ip(addressArr[0])
          .port(Integer.parseInt(addressArr[1]))
          .build();

        if (connectionObj == null) {
          // 机器初始化, 没有连接的情况下, 所以无缓存
          imConnectorMonitorInfo.setConnections(0);
        } else {
          imConnectorMonitorInfo.setConnections(Integer.parseInt(String.valueOf(connectionObj)));
        }
        return imConnectorMonitorInfo;
      })
      .toList();
  }
}
