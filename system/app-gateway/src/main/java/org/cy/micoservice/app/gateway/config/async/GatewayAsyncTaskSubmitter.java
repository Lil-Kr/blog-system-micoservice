package org.cy.micoservice.app.gateway.config.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @Author: Lil-K
 * @Date: 2026/1/3
 * @Description: 异步提交任务
 */
@Slf4j
@Component
public class GatewayAsyncTaskSubmitter {

  private static final int BLOCKING_QUEUE_SIZE_1 = 5000;

  private static final int OVER_LIMIT_QUEUE_SIZE_1 = 4000;

  /** ============================== 线程组-1 ============================== **/
  private final ThreadPoolExecutor executor = new ThreadPoolExecutor (
    4,
    Runtime.getRuntime().availableProcessors() * 2,
    1, // 1 分钟后被销毁
    TimeUnit.MINUTES,
    new ArrayBlockingQueue<>(BLOCKING_QUEUE_SIZE_1),
    r -> {
      Thread t = new Thread(r);
      t.setName("gateway-task-sync-" + t.getId());
      t.setDaemon(false);
      return t;
    },
    new ThreadPoolExecutor.CallerRunsPolicy()
  );

  /**
   * 无返回值, 双层timeout兜底
   * @param name
   * @param task
   * @param fallback
   * @param timeoutMs
   * @return
   */
  public CompletableFuture<Void> runAsync(String name, Runnable task, Runnable fallback, Long timeoutMs) {
    // 1. 队列过载保护
    if (executor.getQueue().size() > OVER_LIMIT_QUEUE_SIZE_1) {
      log.warn("[{}] async queue overloaded, fallback directly", name);
      fallback.run();
      return CompletableFuture.completedFuture(null);
    }

    return CompletableFuture.runAsync(() -> {
        long start = System.currentTimeMillis();
        try {
          task.run();
        } catch (Exception e) {
          log.error("gateway run async task failed: {}", name, e);
          throw e;
        } finally {
          long cost = System.currentTimeMillis() - start;
          if (cost > 200) {
            log.warn("[{}] async task slow:{} ms", name, cost);
          }
        }
      }, executor)
      .orTimeout(timeoutMs, TimeUnit.MILLISECONDS)
      .exceptionally(ex -> {
        log.error("[{}] async task timeout or error", name, ex);
        try {
          fallback.run();
        } catch (Exception fallbackEx) {
          log.error("[{}] fallback failed", name, fallbackEx);
        }
        return null;
      });
  }

  /** ============================== 线程组-2 ============================== **/
  private static final int BLOCKING_QUEUE_SIZE_2 = 5000;

  private static final int OVER_LIMIT_QUEUE_SIZE_2 = 4000;

  private final ThreadPoolExecutor submitTaskPool = new ThreadPoolExecutor (
    4,
    Runtime.getRuntime().availableProcessors() * 2,
    3, // 3 分钟后被销毁
    TimeUnit.MINUTES,
    new ArrayBlockingQueue<>(BLOCKING_QUEUE_SIZE_2),
    r -> {
      Thread t = new Thread(r);
      t.setName("gateway-task-sync-" + t.getId());
      t.setDaemon(false);
      return t;
    },
    new ThreadPoolExecutor.CallerRunsPolicy()
  );

  public <T> CompletableFuture<T> supplyAsync(String taskName, Supplier<T> task, Supplier<T> fallback, Long timeoutMs) {
    // 队列打满时直接走兜底，防止雪崩
    if (submitTaskPool.getQueue().size() > OVER_LIMIT_QUEUE_SIZE_2) {
      log.warn("[{}] async queue overloaded, use fallback", taskName);
      return CompletableFuture.completedFuture(fallback.get());
    }
    return CompletableFuture.supplyAsync(() -> {
        long start = System.currentTimeMillis();
        try {
          return task.get();
        } catch (Exception e) {
          log.error("chat async task failed: {}", taskName, e);
          return fallback.get();
        } finally {
          long cost = System.currentTimeMillis() - start;
          // 如果执行时间过长也要监控到
          if (cost > 200) {
            log.warn("[{}] async task slow: {} ms", taskName, cost);
          }
        }
      }, submitTaskPool)
      .orTimeout(timeoutMs, TimeUnit.MILLISECONDS)
      .exceptionally(ex -> {
        if (ex instanceof TimeoutException) {
          log.warn("[{}] async task timeout after: {} ms", taskName, timeoutMs);
        } else {
          log.error("[{}] async task exception", taskName, ex);
        }
        return fallback.get();
      });
  }
}