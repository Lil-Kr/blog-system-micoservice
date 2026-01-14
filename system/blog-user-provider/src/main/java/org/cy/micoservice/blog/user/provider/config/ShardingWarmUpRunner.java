package org.cy.micoservice.blog.user.provider.config;

import lombok.RequiredArgsConstructor;
import org.cy.micoservice.blog.user.provider.dao.UserMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2026/1/10
 * @Description:
 */
@Component
@RequiredArgsConstructor
public class ShardingWarmUpRunner implements ApplicationRunner {

  private final UserMapper userMapper;

  @Override
  public void run(ApplicationArguments args) {
    // 随便查一条，触发完整初始化
    userMapper.getUserById(0L);
  }
}