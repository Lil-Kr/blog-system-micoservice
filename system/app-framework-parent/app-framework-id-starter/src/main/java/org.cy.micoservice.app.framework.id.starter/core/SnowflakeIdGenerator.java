package org.cy.micoservice.app.framework.id.starter.core;


import org.cy.micoservice.app.framework.id.starter.monitor.IdMonitor;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Description: 秒级雪花ID生成器。
 * 位布局：时间戳29位(基于纪元秒数), 数据中心2位, 工作节点16位, 序列号16位。
 */
public class SnowflakeIdGenerator {

  private final long epochSecond ; // 起始时间
  private final int datacenterId; // 0..3
  private final int workerId;     // 0..65535

  private final int timeBits = 29;
  private final int dcBits = 2;
  private final int workerBits = 16;
  private final int sequenceBits = 16;

  private final long timeMask = (1L << timeBits) - 1;          // 29-bit mask
  private final int dcMask = (1 << dcBits) - 1;                // 0..3
  private final int workerMax = (1 << workerBits);             // size = 65536
  private final int workerValueMask = workerMax - 1;           // 0..65535
  private final int sequenceMask = (1 << sequenceBits) - 1;    // 0..65535

  private final int workerShift = sequenceBits;                // 16
  private final int dcShift = workerBits + sequenceBits;       // 32
  private final int timeShift = dcBits + workerBits + sequenceBits; // 34

  private volatile long lastSecond = -1L;
  private volatile int sequence = 0;

  private final IdMonitor monitor; // 可能为空
  private final String app;
  private final String namespace;

  public SnowflakeIdGenerator(long epochSecond, int datacenterId, int workerId) {
    this(epochSecond, datacenterId, workerId, null, null, null);
  }

  public SnowflakeIdGenerator(long epochSecond, int datacenterId, int workerId, IdMonitor monitor, String app, String namespace) {
    if (datacenterId < 0 || datacenterId > dcMask) {
      throw new IllegalArgumentException("datacenterId out of range: " + datacenterId + ", range=0.." + dcMask);
    }
    if (workerId < 0 || workerId >= workerMax) {
      throw new IllegalArgumentException("workerId out of range: " + workerId + ", range=0.." + (workerMax - 1));
    }
    this.epochSecond = epochSecond;
    this.datacenterId = datacenterId;
    this.workerId = workerId;
    this.monitor = monitor;
    this.app = app == null ? "default" : app;
    this.namespace = namespace == null ? this.app : namespace;
  }

  public synchronized long nextId() {
    long currentSecond = nowSeconds();
    if (currentSecond < lastSecond) { // 说明了发生时间回拨
      long diff = lastSecond - currentSecond;
      if (diff <= 2) {
        long start = System.currentTimeMillis();
        // small clock rollback: wait until lastSecond
        waitUntilSecond(lastSecond);
        currentSecond = lastSecond;
        if (monitor != null) {// 发送监控指标数据
          monitor.onClockRollbackWait(app, namespace, System.currentTimeMillis() - start);
        }
      } else {
        throw new IllegalStateException("System clock moved backwards by " + diff + "s, refusing to generate ID");
      }
    }

    if (currentSecond == lastSecond) {
      sequence = (sequence + 1) & sequenceMask;
      if (sequence == 0) {
        // 当前秒内序列号溢出, 等待至下一秒。
        long start = System.currentTimeMillis();
        currentSecond = waitNextSecond(lastSecond);
        if (monitor != null) {
          monitor.onSeqWait(app, namespace, System.currentTimeMillis() - start);
        }
      }
    } else {
      sequence = 0;
    }

    lastSecond = currentSecond;
    if (monitor != null) {
      monitor.onUpdateLastSecond(app, namespace, lastSecond);
      monitor.onGenerate(app, namespace);
    }

    long diff = currentSecond - epochSecond;
    if (diff < 0) {
      throw new IllegalStateException("Current time before epoch");
    }
    if (diff > timeMask) {
      throw new IllegalStateException("Time exceeds 29-bit window; update epoch or layout");
    }
    long timePart = diff & timeMask;
    return (timePart << timeShift)
      | ((long) (datacenterId & dcMask) << dcShift)
      | ((long) (workerId & workerValueMask) << workerShift)
      | (sequence & sequenceMask);
  }

  private long waitNextSecond(long last) {
    long s;
    do {
      sleepBriefly();
      s = nowSeconds();
    } while (s <= last);
    return s;
  }

  private void waitUntilSecond(long targetSecond) {
    while (nowSeconds() < targetSecond) {
      sleepBriefly();
    }
  }

  private void sleepBriefly() {
    try {
      TimeUnit.MILLISECONDS.sleep(1);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }
  }

  private long nowSeconds() {
    return Instant.now().getEpochSecond();
  }
}