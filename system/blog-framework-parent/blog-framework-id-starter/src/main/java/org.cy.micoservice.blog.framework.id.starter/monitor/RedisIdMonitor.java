package org.cy.micoservice.blog.framework.id.starter.monitor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Lil-K
 * @Description: 使用Redis快照的实例级观察监控器
 * 定期将当前计数器连同TTL刷新至Redis, 因此扫描键即可显示在线实例
 */
public class RedisIdMonitor implements IdMonitor {

  private static final Logger log = LoggerFactory.getLogger(RedisIdMonitor.class);

  private final StringRedisTemplate redisTemplate;
  private final String prefix; // e.g., "monitor"
  private final String app;
  private final String namespace;
  private final String instanceId;
  private final int ttlSeconds;
  private final int flushSeconds;

  private final AtomicInteger workerIdGauge = new AtomicInteger(-1);
  private final AtomicLong generateCount = new AtomicLong();
  private final AtomicLong seqWaitCount = new AtomicLong();
  private final AtomicLong seqWaitMillis = new AtomicLong();
  private final AtomicLong rollbackWaitCount = new AtomicLong();
  private final AtomicLong rollbackWaitMillis = new AtomicLong();
  private final AtomicLong lastSecondGauge = new AtomicLong(-1);
  private final AtomicLong lastFlushEpochSec = new AtomicLong(0);

  private final ScheduledExecutorService scheduler;

  public RedisIdMonitor(StringRedisTemplate redisTemplate,
                        String prefix,
                        String app,
                        String namespace,
                        int flushSeconds,
                        int ttlSeconds) {
    this.redisTemplate = redisTemplate;
    this.prefix = prefix == null ? "monitor" : prefix;
    this.app = app == null ? "app-unknown" : app;
    this.namespace = namespace == null ? this.app : namespace;
    this.ttlSeconds = Math.max(30, ttlSeconds);
    this.flushSeconds = Math.max(5, flushSeconds);
    this.instanceId = buildInstanceId();
    this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
      Thread t = new Thread(r, "id-monitor-redis");
      t.setDaemon(true);
      return t;
    });
    this.scheduler.scheduleAtFixedRate(this::flushSnapshot, this.flushSeconds, this.flushSeconds, TimeUnit.SECONDS);
  }

  private String buildInstanceId() {
    String host = "unknown-host";
    try {
      host = InetAddress.getLocalHost().getHostName();
    } catch (Exception ignore) {
    }
    String pid = "unknown-pid";
    try {
      pid = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
    } catch (Exception ignore) {
    }
    return host + "|" + pid + "|" + UUID.randomUUID();
  }

  private String instanceKey() {
    return prefix + ":id:inst:" + namespace + ":" + app + ":" + instanceId;
  }

  private void flushSnapshot() {
    try {
      String key = instanceKey();
      Map<String, String> map = new HashMap<>();
      map.put("app", app);
      map.put("namespace", namespace);
      map.put("instanceId", instanceId);
      map.put("workerId", String.valueOf(workerIdGauge.get()));
      map.put("generateCount", String.valueOf(generateCount.get()));
      map.put("seqWaitCount", String.valueOf(seqWaitCount.get()));
      map.put("seqWaitMillis", String.valueOf(seqWaitMillis.get()));
      map.put("rollbackWaitCount", String.valueOf(rollbackWaitCount.get()));
      map.put("rollbackWaitMillis", String.valueOf(rollbackWaitMillis.get()));
      map.put("lastSecond", String.valueOf(lastSecondGauge.get()));
      long nowSec = System.currentTimeMillis() / 1000L;
      lastFlushEpochSec.set(nowSec);
      map.put("lastFlushEpochSec", String.valueOf(nowSec));
      redisTemplate.opsForHash().putAll(key, map);
      redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.warn("{}|app={} namespace={} event=monitor.flush.fail msg={}", prefix, app, namespace, e.getMessage());
    }
  }

  @Override
  public void onAllocateSuccess(String app, String namespace, int workerId) {
    workerIdGauge.set(workerId);
    flushSnapshot();
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
    if (waitMillis > 0) {
      seqWaitMillis.addAndGet(waitMillis);
    }
  }

  @Override
  public void onClockRollbackWait(String app, String namespace, long waitMillis) {
    rollbackWaitCount.incrementAndGet();
    if (waitMillis > 0) {
      rollbackWaitMillis.addAndGet(waitMillis);
    }
  }

  @Override
  public void onUpdateLastSecond(String app, String namespace, long second) {
    lastSecondGauge.set(second);
  }

  @Override
  public IdObservationDTO snapshot() {
    IdObservationDTO dto = new IdObservationDTO();
    dto.setApp(app);
    dto.setNamespace(namespace);
    dto.setInstanceId(instanceId);
    dto.setWorkerId(workerIdGauge.get());
    dto.setGenerateCount(generateCount.get());
    dto.setSeqWaitCount(seqWaitCount.get());
    dto.setSeqWaitMillis(seqWaitMillis.get());
    dto.setRollbackWaitCount(rollbackWaitCount.get());
    dto.setRollbackWaitMillis(rollbackWaitMillis.get());
    dto.setLastSecond(lastSecondGauge.get());
    dto.setLastFlushEpochSec(lastFlushEpochSec.get());
    return dto;
  }

  @Override
  public void close() {
    try {
      flushSnapshot();
    } catch (Exception ignore) {
    }
    scheduler.shutdownNow();
  }
}
