package org.cy.micoservice.blog.framework.id.starter.service;


import org.cy.micoservice.blog.framework.id.starter.core.SnowflakeIdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Lil-K
 * @Description: 小绿书的id服务
 */
public class DefaultIdService implements IdService {

  private final SnowflakeIdGenerator generator;

  public DefaultIdService(SnowflakeIdGenerator generator) {
    this.generator = generator;
  }

  @Override
  public long getId() {
    return generator.nextId();
  }

  @Override
  public List<Long> getBatch(int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("size must be > 0");
    }
    List<Long> list = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      list.add(generator.nextId());
    }
    return list;
  }

}
