package org.cy.micoservice.app.framework.id.starter.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Lil-K
 * @Description: 分布式id自动装载配置
 */
@ConfigurationProperties(prefix = "blog.id")
public class IdProperties {

  /** Enable id generator */
  private boolean enabled = true;

  /** Custom namespace; default fallback to spring.application.name */
  private String namespace;

  /** epoch in seconds; default 2025-01-01 00:00:00 UTC */
  private long epochSecond = 1735689600L;

  /** fail on Redis unavailable at startup */
  private boolean failOnRedisUnavailable = true;

  /** datacenter id (2 bits, range 0..3), default 0 */
  private int datacenterId = 0;

  public int getDatacenterId() { return datacenterId; }
  public void setDatacenterId(int datacenterId) { this.datacenterId = datacenterId; }

  /** monitoring config (no Micrometer) */
  private boolean monitorEnabled = true; // enable simple logging monitor by default
  private int monitorIntervalSeconds = 60; // log summary every N seconds
  private String monitorPrefix = "monitor"; // required prefix
  private int monitorTtlSeconds = 180; // instance snapshot TTL

  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean enabled) { this.enabled = enabled; }

  public String getNamespace() { return namespace; }
  public void setNamespace(String namespace) { this.namespace = namespace; }

  public long getEpochSecond() { return epochSecond; }
  public void setEpochSecond(long epochSecond) { this.epochSecond = epochSecond; }

  public boolean isFailOnRedisUnavailable() { return failOnRedisUnavailable; }
  public void setFailOnRedisUnavailable(boolean failOnRedisUnavailable) { this.failOnRedisUnavailable = failOnRedisUnavailable; }

  public boolean isMonitorEnabled() { return monitorEnabled; }
  public void setMonitorEnabled(boolean monitorEnabled) { this.monitorEnabled = monitorEnabled; }

  public int getMonitorIntervalSeconds() { return monitorIntervalSeconds; }
  public void setMonitorIntervalSeconds(int monitorIntervalSeconds) { this.monitorIntervalSeconds = monitorIntervalSeconds; }

  public String getMonitorPrefix() { return monitorPrefix; }
  public void setMonitorPrefix(String monitorPrefix) { this.monitorPrefix = monitorPrefix; }

  public int getMonitorTtlSeconds() { return monitorTtlSeconds; }
  public void setMonitorTtlSeconds(int monitorTtlSeconds) { this.monitorTtlSeconds = monitorTtlSeconds; }
}