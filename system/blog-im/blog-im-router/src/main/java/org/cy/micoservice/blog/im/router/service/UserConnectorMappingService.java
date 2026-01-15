package org.cy.micoservice.blog.im.router.service;

/**
 * @Author: Lil-K
 * @Date: 2025/12/16
 * @Description: 用户与具体im机器的映射关系
 */
public interface UserConnectorMappingService {

  /**
   * 保存用户id和所连接的机器ip地址
   * @param userId
   * @param connectorAddress
   * @return
   */
  boolean saveAddressByUserId(Long userId, String connectorAddress);

  /**
   * 移除指定用户id所在的机器ip地址记录
   * @param userId
   */
  void remove(Long userId);

  /**
   * 根据用户id获取机器ip
   * @param userId
   * @return
   */
  String getAddressByUserId(Long userId);
}