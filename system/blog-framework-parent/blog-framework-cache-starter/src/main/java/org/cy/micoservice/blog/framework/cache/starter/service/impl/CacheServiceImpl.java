package org.cy.micoservice.blog.framework.cache.starter.service.impl;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.framework.cache.starter.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/6/28
 * @Description: 缓存封装组件
 */
@Component
public class CacheServiceImpl<T> implements CacheService<T> {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Override
  public T getOne(String key, int expireTime, TimeUnit timeUnit, Supplier<T> supplier) {
    if (StringUtils.isEmpty(key)) {
      return null;
    }
    if (expireTime <= 0) {
      throw new IllegalArgumentException("expireTime must be greater than 0");
    }
    String cacheValueStr = redisTemplate.opsForValue().get(key);
    if (!StringUtils.isEmpty(cacheValueStr)) {
      return (T) cacheValueStr;
    }
    synchronized (CacheService.class) {
      cacheValueStr = redisTemplate.opsForValue().get(key);
      if (!StringUtils.isEmpty(cacheValueStr)) {
        return (T) cacheValueStr;
      }
      //不能做到100%的单请求查询db控制, 还是会有疏漏
      T queryObj = supplier.get();
      if (queryObj == null) {
        return null;
      }
      redisTemplate.opsForValue().set(key, JSON.toJSONString(queryObj), expireTime, timeUnit);
      return queryObj;
    }
  }

  @Override
  public T getOne(String key, int expireTime, TimeUnit timeUnit, Supplier<T> supplier, Class<T> clazz) {
    if (StringUtils.isEmpty(key)) {
      return null;
    }
    if (expireTime <= 0) {
      throw new IllegalArgumentException("expireTime must be greater than 0");
    }
    String cacheValueStr = redisTemplate.opsForValue().get(key);
    if (!StringUtils.isEmpty(cacheValueStr)) {
      return JSON.parseObject(cacheValueStr, clazz);
    }
    //热点数据（大V用户, 很多请求打入到db上）
    //并发问题
    //如果缓存命中失败
    synchronized (CacheServiceImpl.class) {
      cacheValueStr = redisTemplate.opsForValue().get(key);
      if (!StringUtils.isEmpty(cacheValueStr)) {
        return JSON.parseObject(cacheValueStr, clazz);
      }
      //不能做到100%的单请求查询db控制, 还是会有疏漏
      T queryObj = supplier.get();
      if (queryObj == null) {
        return null;
      }
      redisTemplate.opsForValue().set(key, JSON.toJSONString(queryObj), expireTime, timeUnit);
      return queryObj;
    }
  }

  @Override
  public List<T> getMulti(List<String> keys, int expireTime, TimeUnit timeUnit, Function<List<String>, Map<String, T>> function, Class<T> clazz) {
    if (keys == null || keys.isEmpty()) {
      return Collections.emptyList();
    }
    List<String> resultList = redisTemplate.opsForValue().multiGet(keys);
    if (CollectionUtils.isEmpty(resultList)) {
      return Collections.emptyList();
    }
    int i = 0;
    List<T> finalResultList = new ArrayList<>();
    List<String> missCacheKeys = new ArrayList<>();
    for (String json : resultList) {
      String cacheKey = keys.get(i);
      if (json == null) {
        missCacheKeys.add(cacheKey);
      } else {
        finalResultList.add(JSON.parseObject(json, clazz));
      }
      i++;
    }
    //缺失缓存的部分查询db补充
    if (!CollectionUtils.isEmpty(missCacheKeys)) {
      synchronized (CacheServiceImpl.class) {
        //todo missCache的二次查询命中确认
        Map<String, T> queryObjs = function.apply(missCacheKeys);
        Map<String, String> cacheMap = new HashMap<>();
        for (String cacheKey : queryObjs.keySet()) {
          T missCacheObj = queryObjs.get(cacheKey);
          cacheMap.put(cacheKey, JSON.toJSONString(missCacheObj));
          finalResultList.add(missCacheObj);
        }
        redisTemplate.opsForValue().multiSet(cacheMap);
        redisTemplate.executePipelined(new SessionCallback<Object>() {
          @Override
          public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
            for (String redisKey : cacheMap.keySet()) {
              operations.expire((K) redisKey, expireTime, timeUnit);
            }
            return null;
          }
        });
      }
    }
    return finalResultList;
  }

  @Override
  public void removeCacheKey(String cacheKey) {
    redisTemplate.delete(cacheKey);
  }
}
