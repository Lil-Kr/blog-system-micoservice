package org.cy.micoservice.blog.im.gateway.service;

import org.cy.micoservice.blog.im.gateway.dto.ImConnectorMonitor;

import java.util.List;
import java.util.Set;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: 存储下游存活连接节点信息
 */
public interface ImConnectorMonitorService {

  /**
   * 获取下游 im-connector 所有节点的监控配置信息
   * @return
   */
   List<ImConnectorMonitor> getImConnectorMonitorAllList();

  /**
   * 刷新本地内存
   * @param nodeAddressSet
   */
  void refreshCache(Set<String> nodeAddressSet);
}