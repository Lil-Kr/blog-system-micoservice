package org.cy.micoservice.blog.im.connector.config.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description: 异步任务提交者
 */
@Slf4j
@Component
public class ImPushAsyncTaskSubmitter {

  private final ThreadPoolExecutor submitTaskPool = new ThreadPoolExecutor(2,
    Runtime.getRuntime().availableProcessors() * 2,
      3,
      TimeUnit.MINUTES,
      new ArrayBlockingQueue<>(5000));

  public void submit(Runnable runnable) {
    try {
      submitTaskPool.execute(runnable);
    } catch (Exception e) {
      log.error("im push async task submit failed", e);
    }
  }
}