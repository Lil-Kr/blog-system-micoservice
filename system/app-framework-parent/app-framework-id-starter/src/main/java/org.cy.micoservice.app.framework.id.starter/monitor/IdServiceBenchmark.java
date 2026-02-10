package org.cy.micoservice.app.framework.id.starter.monitor;


import org.cy.micoservice.app.framework.id.starter.service.IdService;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 压测工具：用于评估 IdService#getId() 在指定并发与时间下的吞吐表现。
 * 单一职责：仅提供压测方法, 不引入额外依赖, 也不自动触发执行。
 */
public final class IdServiceBenchmark {


  /**
   * 便捷方法：无预热。
   */
  public static BenchmarkResult benchmarkGetId(IdService idService, int threads, int durationSeconds) {
    return benchmarkGetId(idService, threads, durationSeconds, 0);
  }

  /**
   * 对 IdService#getId() 进行压测。
   *
   * @param idService       IdService 实例(例如通过 Spring 注入)
   * @param threads         并发线程数(>=1)
   * @param durationSeconds 压测持续时间(秒, >=1)
   * @param warmupSeconds   预热时间(秒, >=0)
   * @return BenchmarkResult 结果数据(总请求、耗时、QPS 等)
   */
  public static BenchmarkResult benchmarkGetId(IdService idService,
                                               int threads,
                                               int durationSeconds,
                                               int warmupSeconds) {
    Objects.requireNonNull(idService, "idService must not be null");
    if (threads <= 0) {
      throw new IllegalArgumentException("threads must be > 0");
    }
    if (durationSeconds <= 0) {
      throw new IllegalArgumentException("durationSeconds must be > 0");
    }
    if (warmupSeconds < 0) {
      throw new IllegalArgumentException("warmupSeconds must be >= 0");
    }

    // 预热：触发类加载与JIT, 避免测量阶段受冷启动干扰
    if (warmupSeconds > 0) {
      final long warmupEnd = System.nanoTime() + TimeUnit.SECONDS.toNanos(warmupSeconds);
      long spin = 0;
      while (System.nanoTime() < warmupEnd) {
        idService.getId();
        // 轻度让步, 避免单核环境长时间独占
        if ((++spin & 0xFFFF) == 0) {
          Thread.yield();
        }
      }
    }

    final ExecutorService pool = Executors.newFixedThreadPool(threads, r -> {
      Thread t = new Thread(r, "id-bench-worker");
      t.setDaemon(true);
      return t;
    });
    final CountDownLatch startLatch = new CountDownLatch(1);
    final CountDownLatch doneLatch = new CountDownLatch(threads);
    final AtomicBoolean stop = new AtomicBoolean(false);
    final java.util.concurrent.atomic.LongAdder totalOps = new java.util.concurrent.atomic.LongAdder();

    for (int i = 0; i < threads; i++) {
      pool.execute(() -> {
        try {
          startLatch.await();
          long cnt = 0;
          while (!stop.get()) {
            idService.getId();
            cnt++;
          }
          totalOps.add(cnt);
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
        } finally {
          doneLatch.countDown();
        }
      });
    }

    final long startNs = System.nanoTime();
    startLatch.countDown();
    try {
      TimeUnit.SECONDS.sleep(durationSeconds);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    stop.set(true);

    try {
      doneLatch.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    final long endNs = System.nanoTime();
    pool.shutdownNow();

    final long durationNs = Math.max(1L, endNs - startNs);
    final long ops = totalOps.sum();
    final double seconds = durationNs / 1_000_000_000.0;
    final double qps = ops / seconds;

    return new BenchmarkResult(ops, durationNs, threads, qps);
  }

  /**
   * 压测结果。
   */
  public static final class BenchmarkResult {
    private final long totalOps;
    private final long durationNanos;
    private final int threads;
    private final double qps;

    public BenchmarkResult(long totalOps, long durationNanos, int threads, double qps) {
      this.totalOps = totalOps;
      this.durationNanos = durationNanos;
      this.threads = threads;
      this.qps = qps;
    }

    public long getTotalOps() {
      return totalOps;
    }

    public long getDurationNanos() {
      return durationNanos;
    }

    public int getThreads() {
      return threads;
    }

    public double getQps() {
      return qps;
    }

    public double getDurationSeconds() {
      return durationNanos / 1_000_000_000.0;
    }

    public double getAvgOpsPerThread() {
      return threads <= 0 ? 0 : ((double) totalOps) / threads;
    }

    @Override
    public String toString() {
      return "BenchmarkResult{" +
        "totalOps=" + totalOps +
        ", durationSeconds=" + getDurationSeconds() +
        ", threads=" + threads +
        ", qps=" + qps +
        '}';
    }
  }
}
