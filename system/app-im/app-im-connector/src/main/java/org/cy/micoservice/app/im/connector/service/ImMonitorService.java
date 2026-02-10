package org.cy.micoservice.app.im.connector.service;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: im连接层的监控service
 */
public interface ImMonitorService {

  /**
   * 重制连接信息
   */
  void initConnection();

  /**
   * 增加连接数
   */
  void incrConnection();

  /**
   * 减少连接数
   */
  void decrConnection();
}