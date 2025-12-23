package org.cy.micoservice.blog.framework.id.starter.core;

/**
 * @Author: Lil-K
 * @Description: id分配器。
 */
public interface WorkerIdAllocator {

  /**
   * 分配id资源
   *
   * @return
   */
  int allocateOrFail();
}
