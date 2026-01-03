package org.cy.micoservice.blog.gateway.config.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Author: Lil-K
 * @Date: 2026/1/3
 * @Description: 异步提交任务
 */
@Slf4j
@Component
public class GatewayAsyncTaskSubmitter {

  private final ThreadPoolExecutor submitTaskPool = new ThreadPoolExecutor (
    4,
    Runtime.getRuntime().availableProcessors() * 2,
    1, // 1 分钟后被销毁
    TimeUnit.MINUTES,
    new ArrayBlockingQueue<>(5000),
    r -> {
      Thread t = new Thread(r);
      t.setName("gateway-task-sync-" + t.getId());
      t.setDaemon(false);
      return t;
    },
    new ThreadPoolExecutor.CallerRunsPolicy()
  );

  public CompletableFuture<Void> runAsync(String name, Runnable task) {
    return CompletableFuture.runAsync(() -> {
      try {
        task.run();
      } catch (Exception e) {
        log.error("gateway run async task failed: {}", name, e);
        throw e;
      }
    }, submitTaskPool);
  }

  public <T> CompletableFuture<T> supplyAsync(String name, Supplier<T> task) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return task.get();
      } catch (Exception e) {
        log.error("gateway run async task failed: {}", name, e);
        throw e;
      }
    }, submitTaskPool);
  }
}