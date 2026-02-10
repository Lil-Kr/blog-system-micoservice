package org.cy.micoservice.app.framework.id.starter.core;


import org.springframework.util.Assert;

/**
 * @Author: Lil-K
 * @Description: workerId分配器的抽象基类
 */
public abstract class AbstractWorkerIdAllocator implements WorkerIdAllocator {

  protected final String namespace;
  protected final int maxWorker;

  protected AbstractWorkerIdAllocator(String namespace, int maxWorker) {
    Assert.hasText(namespace, "namespace must not be empty");
    if (maxWorker <= 0) {
      throw new IllegalArgumentException("maxWorker must be > 0");
    }
    this.namespace = namespace;
    this.maxWorker = maxWorker;
  }

  public String getNamespace() {
    return namespace;
  }

}