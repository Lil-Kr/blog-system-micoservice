package org.cy.micoservice.blog.user.provider.service;

import org.cy.micoservice.blog.entity.user.model.provider.pojo.User;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 用户服务接口层
 */
public interface UserShardService {

  /**
   * 批量查询用户信息
   * @param userIds
   * @return
   */
  List<User> queryInUserIds(List<Long> userIds);
}