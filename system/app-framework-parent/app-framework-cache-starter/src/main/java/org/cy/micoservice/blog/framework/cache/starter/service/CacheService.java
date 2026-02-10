package org.cy.micoservice.blog.framework.cache.starter.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/6/28
 * @Description: 标准缓存访问service
 */
public interface CacheService<T> {

  /**
   * 单个对象从缓存查询
   *
   * @param key
   * @param expireTime
   * @param timeUnit
   * @param supplier
   * @return
   */
  T getOne(String key, int expireTime, TimeUnit timeUnit, Supplier<T> supplier);

  /**
   * 单个对象从缓存查询
   *
   * @param key
   * @param expireTime
   * @param timeUnit
   * @param supplier
   * @return
   */
  T getOne(String key, int expireTime, TimeUnit timeUnit, Supplier<T> supplier,Class<T> clazz);

  /**
   * 多个对象从缓存查询
   *
   * @param keys
   * @param expireTime
   * @param timeUnit
   * @param function
   * @param clazz
   * @return
   */
  List<T> getMulti(List<String> keys, int expireTime, TimeUnit timeUnit, Function<List<String>, Map<String, T>> function
    , Class<T> clazz);

  /**
   * 删除缓存key
   *
   * @param cacheKey
   */
  void removeCacheKey(String cacheKey);
}
