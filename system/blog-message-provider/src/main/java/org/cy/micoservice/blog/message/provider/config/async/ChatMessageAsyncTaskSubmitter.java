package org.cy.micoservice.blog.message.provider.config.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Author: Lil-K
 * @Date: 2025/12/30
 * @Description: 异步提交任务
 */
@Slf4j
@Component
public class ChatMessageAsyncTaskSubmitter {

  private final ThreadPoolExecutor submitTaskPool = new ThreadPoolExecutor(
      4,
      Runtime.getRuntime().availableProcessors() * 2,
      3, // 3 分钟后被销毁
      TimeUnit.MINUTES,
      new ArrayBlockingQueue<>(5000),
      r -> {
        Thread t = new Thread(r);
        t.setName("chat-message-sync-" + t.getId());
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
        log.error("chat message flush into DB task failed: {}", name, e);
        throw e;
      }
    }, submitTaskPool);
  }

  public <T> CompletableFuture<T> supplyAsync(String name, Supplier<T> task) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return task.get();
      } catch (Exception e) {
        log.error("chat async task failed: {}", name, e);
        throw e;
      }
    }, submitTaskPool);
  }
}
