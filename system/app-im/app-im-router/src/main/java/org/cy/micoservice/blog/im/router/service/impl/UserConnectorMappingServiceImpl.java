package org.cy.micoservice.blog.im.router.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.im.router.config.ImRouterRedisKeyBuilder;
import org.cy.micoservice.blog.im.router.service.UserConnectorMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description:
 */
@Service
public class UserConnectorMappingServiceImpl implements UserConnectorMappingService {

  @Autowired
  private RedisTemplate<String, String> stringRedisTemplate;
  @Autowired
  private ImRouterRedisKeyBuilder imRouterRedisKeyBuilder;

  /**
   * 保存用户id和所连接的机器ip地址
   * @param userId
   * @param connectorAddress
   * @return
   */
  @Override
  public boolean saveAddressByUserId(Long userId, String connectorAddress) {
    if (userId == null || StringUtils.isBlank(connectorAddress)) {
      return false;
    }
    String cacheKey = imRouterRedisKeyBuilder.imUserConnectorKey(String.valueOf(userId));
    /**
     * todo: 临时存60分钟, 后期优化
     */
    stringRedisTemplate.opsForValue().set(cacheKey, connectorAddress, 60, TimeUnit.MINUTES);
    return true;
  }

  /**
   * 移除指定用户id所在的机器ip地址记录
   * @param userId
   */
  @Override
  public void remove(Long userId) {
    if (userId == null) return;
    String loginCacheKey = imRouterRedisKeyBuilder.imUserConnectorKey(String.valueOf(userId));
    stringRedisTemplate.delete(loginCacheKey);
  }

  /**
   * 根据用户id获取机器ip
   * @param userId
   * @return
   */
  @Override
  public String getAddressByUserId(Long userId) {
    if (userId == null) return null;
    String cacheKey = imRouterRedisKeyBuilder.imUserConnectorKey(String.valueOf(userId));
    return stringRedisTemplate.opsForValue().get(cacheKey);
  }
}
