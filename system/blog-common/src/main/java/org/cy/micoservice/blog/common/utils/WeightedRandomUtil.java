package org.cy.micoservice.blog.common.utils;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 通用权重随机选择工具类 支持K-V结构存储、动态权重配置和更新
 */
public class WeightedRandomUtil {

  // 存储键值对
  private final Map<String, Object> dataMap = new HashMap<>();
  // 存储权重配置
  private final Map<String, Integer> weightMap = new HashMap<>();
  // 缓存的前缀和数组(优化查询性能)
  private volatile String[] keys;
  private volatile int[] prefixWeights;
  private volatile int totalWeight;
  // 读写锁保证线程安全
  private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
  private final Random random = new Random();

  /**
   * 添加/更新键值对和权重
   * @param key    键
   * @param value  值
   * @param weight 权重(必须>0)
   */
  public void put(String key, Object value, int weight) {
    if (key == null || value == null) {
      throw new IllegalArgumentException("Key和Value不能为空");
    }
    if (weight <= 0) {
      throw new IllegalArgumentException("权重必须大于0");
    }

    rwLock.writeLock().lock();
    try {
      dataMap.put(key, value);
      weightMap.put(key, weight);
      // 重建前缀和数组
      rebuildPrefixWeights();
    } finally {
      rwLock.writeLock().unlock();
    }
  }

  /**
   * 批量添加/更新键值对和权重
   * @param entries 包含键、值、权重的条目列表
   */
  public void putAll(Map<String, Entry> entries) {
    if (entries == null || entries.isEmpty()) {
      return;
    }

    rwLock.writeLock().lock();
    try {
      for (Map.Entry<String, Entry> entry : entries.entrySet()) {
        String key = entry.getKey();
        Entry valueEntry = entry.getValue();
        dataMap.put(key, valueEntry.value);
        weightMap.put(key, valueEntry.weight);
      }
      rebuildPrefixWeights();
    } finally {
      rwLock.writeLock().unlock();
    }
  }

  /**
   * 更新指定Key的权重
   * @param key    键
   * @param weight 新权重(必须>0)
   */
  public void updateWeight(String key, int weight) {
    if (weight <= 0) {
      throw new IllegalArgumentException("权重必须大于0");
    }
    if (!weightMap.containsKey(key)) {
      throw new NoSuchElementException("Key不存在: " + key);
    }

    rwLock.writeLock().lock();
    try {
      weightMap.put(key, weight);
      rebuildPrefixWeights();
    } finally {
      rwLock.writeLock().unlock();
    }
  }

  /**
   * 增加权重
   * @param key
   * @param weight
   */
  public void incrementWeight(String key, int weight) {
    if (!weightMap.containsKey(key)) {
      throw new NoSuchElementException("Key不存在: " + key);
    }
    rwLock.writeLock().lock();
    try {
      Integer currentWeight = weightMap.get(key);
      Integer newWeight = currentWeight + weight;
      weightMap.put(key, newWeight);
      rebuildPrefixWeights();
    } finally {
      rwLock.writeLock().unlock();
    }
  }

  /**
   * 减少权重
   * @param key
   * @param weight
   */
  public void decrementWeight(String key, int weight) {
    this.incrementWeight(key, -1 * weight);
  }

  /**
   * 根据权重随机选择一个Value
   * @return 随机选中的Value
   */
  public Object select() {
    String key = selectKey();
    return key != null ? dataMap.get(key) : null;
  }

  /**
   * 根据权重随机选择一个Key
   * @return 随机选中的Key
   */
  public String selectKey() {
    rwLock.readLock().lock();
    try {
      if (totalWeight <= 0 || keys == null || keys.length == 0) {
        throw new IllegalStateException("没有配置任何权重数据");
      }

      // 生成随机数
      int randomValue = random.nextInt(totalWeight) + 1;

      // 二分查找找到对应的Key
      int left = 0;
      int right = prefixWeights.length - 1;
      while (left < right) {
        int mid = left + (right - left) / 2;
        if (prefixWeights[mid] < randomValue) {
          left = mid + 1;
        } else {
          right = mid;
        }
      }

      return keys[left];
    } finally {
      rwLock.readLock().unlock();
    }
  }

  /**
   * 批量选择Value
   * @param count 选择数量
   * @return Value列表
   */
  public List<Object> selectMultiple(int count) {
    if (count <= 0) {
      throw new IllegalArgumentException("选择数量必须大于0");
    }

    List<Object> result = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      result.add(select());
    }
    return result;
  }

  /**
   * 获取指定Key的Value
   * @param key 键
   * @return 值
   */
  public Object get(String key) {
    rwLock.readLock().lock();
    try {
      return dataMap.get(key);
    } finally {
      rwLock.readLock().unlock();
    }
  }

  /**
   * 获取指定Key的权重
   * @param key 键
   * @return 权重值
   */
  public Integer getWeight(String key) {
    rwLock.readLock().lock();
    try {
      return weightMap.get(key);
    } finally {
      rwLock.readLock().unlock();
    }
  }

  /**
   * 移除指定Key
   *
   * @param key 键
   */
  public void remove(String key) {
    rwLock.writeLock().lock();
    try {
      dataMap.remove(key);
      weightMap.remove(key);
      rebuildPrefixWeights();
    } finally {
      rwLock.writeLock().unlock();
    }
  }

  /**
   * 清空所有数据
   */
  public void clear() {
    rwLock.writeLock().lock();
    try {
      dataMap.clear();
      weightMap.clear();
      keys = null;
      prefixWeights = null;
      totalWeight = 0;
    } finally {
      rwLock.writeLock().unlock();
    }
  }

  /**
   * 获取所有Key
   *
   * @return Key集合
   */
  public Set<String> keySet() {
    rwLock.readLock().lock();
    try {
      return new HashSet<>(dataMap.keySet());
    } finally {
      rwLock.readLock().unlock();
    }
  }

  /**
   * 获取数据大小
   *
   * @return 键值对数量
   */
  public int size() {
    rwLock.readLock().lock();
    try {
      return dataMap.size();
    } finally {
      rwLock.readLock().unlock();
    }
  }

  /**
   * 重建前缀和数组(内部使用)
   */
  private void rebuildPrefixWeights() {
    int size = weightMap.size();
    if (size == 0) {
      keys = null;
      prefixWeights = null;
      totalWeight = 0;
      return;
    }

    keys = new String[size];
    prefixWeights = new int[size];
    int index = 0;
    int currentTotal = 0;

    for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {
      keys[index] = entry.getKey();
      currentTotal += entry.getValue();
      prefixWeights[index] = currentTotal;
      index++;
    }

    totalWeight = currentTotal;
  }

  /**
   * 内部静态类, 用于批量操作
   */
  public static class Entry {
    private final Object value;
    private final int weight;

    public Entry(Object value, int weight) {
      this.value = value;
      this.weight = weight;
    }

    public static Entry of(Object value, int weight) {
      return new Entry(value, weight);
    }
  }

  // -------------------------- 静态工具方法封装 --------------------------

  private static final WeightedRandomUtil DEFAULT_INSTANCE = new WeightedRandomUtil();

  /**
   * 获取默认实例(单例)
   *
   * @return 默认权重工具实例
   */
  public static WeightedRandomUtil getInstance() {
    return DEFAULT_INSTANCE;
  }

  /**
   * 创建新实例
   *
   * @return 新的权重工具实例
   */
  public static WeightedRandomUtil createNewInstance() {
    return new WeightedRandomUtil();
  }

  /**
   * 静态方法：向默认实例添加数据
   */
  public static void putToDefault(String key, Object value, int weight) {
    DEFAULT_INSTANCE.put(key, value, weight);
  }

  /**
   * 静态方法：从默认实例随机选择
   */
  public static Object selectFromDefault() {
    return DEFAULT_INSTANCE.select();
  }
}