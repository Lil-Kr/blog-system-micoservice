package org.cy.micoservice.blog.framework.id.starter.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于日志的轻量级监控器。无需外部指标依赖。
 * 定期打印摘要信息，前缀为"monitor"并附带应用标签。
 */
public class LoggingIdMonitor implements IdMonitor {

  private static final Logger log = LoggerFactory.getLogger(LoggingIdMonitor.class);

  private final String prefix; // e.g., "monitor"
  private final String app;    // application name
  private final String namespace;

  private final AtomicLong workerIdGauge = new AtomicLong(-1);
  private final AtomicLong generateCount = new AtomicLong();
  private final AtomicLong seqWaitCount = new AtomicLong();
  private final AtomicLong seqWaitMillis = new AtomicLong();
  private final AtomicLong rollbackWaitCount = new AtomicLong();
  private final AtomicLong rollbackWaitMillis = new AtomicLong();
  private final AtomicLong lastSecondGauge = new AtomicLong(-1);

  private final ScheduledExecutorService scheduler;

  public LoggingIdMonitor(String prefix, String app, String namespace, int intervalSeconds) {
    this.prefix = prefix == null ? "monitor" : prefix;
    this.app = app == null ? "app-unknown" : app;
    this.namespace = namespace == null ? "default" : namespace;
    this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
      Thread t = new Thread(r, "id-monitor-logger");
      t.setDaemon(true);
      return t;
    });
    int interval = Math.max(5, intervalSeconds);
    this.scheduler.scheduleAtFixedRate(this::logSummary, interval, interval, TimeUnit.SECONDS);
  }

  private void logSummary() {
    log.info("{}|app={} namespace={} workerId={} gen={} seqWait.count={} seqWait.ms={} rollback.count={} rollback.ms={} lastSecond={}",
      prefix, app, namespace, workerIdGauge.get(), generateCount.get(),
      seqWaitCount.get(), seqWaitMillis.get(),
      rollbackWaitCount.get(), rollbackWaitMillis.get(),
      lastSecondGauge.get());
  }

  @Override
  public void onAllocateSuccess(String app, String namespace, int workerId) {
    workerIdGauge.set(workerId);
    log.info("{}|app={} namespace={} event=allocate.success workerId={}", prefix, app, namespace, workerId);
  }

  @Override
  public void onAllocateFail(String app, String namespace, String error) {
    log.error("{}|app={} namespace={} event=allocate.fail error={}", prefix, app, namespace, error);
  }

  @Override
  public void onGenerate(String app, String namespace) {
    generateCount.incrementAndGet();
  }

  @Override
  public void onSeqWait(String app, String namespace, long waitMillis) {
    seqWaitCount.incrementAndGet();
    if (waitMillis > 0) seqWaitMillis.addAndGet(waitMillis);
  }

  @Override
  public void onClockRollbackWait(String app, String namespace, long waitMillis) {
    rollbackWaitCount.incrementAndGet();
    if (waitMillis > 0) rollbackWaitMillis.addAndGet(waitMillis);
  }

  @Override
  public void onUpdateLastSecond(String app, String namespace, long second) {
    lastSecondGauge.set(second);
  }

  @Override
  public void close() {
    scheduler.shutdownNow();
  }
}
