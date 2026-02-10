package org.cy.micoservice.app.user.provider.service.impl;

import org.cy.micoservice.app.entity.user.model.provider.pojo.User;
import org.cy.micoservice.app.user.provider.service.UserShardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
@Service
public class UserShardServiceImpl implements UserShardService {
  @Override
  public List<User> queryInUserIds(List<Long> userIds) {
    return new ArrayList<>();
  }
}
