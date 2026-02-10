package org.cy.micoservice.app.framework.id.starter.autoconfigure;

import org.cy.micoservice.app.framework.id.starter.core.SimpleRedisWorkerIdAllocator;
import org.cy.micoservice.app.framework.id.starter.core.SnowflakeIdGenerator;
import org.cy.micoservice.app.framework.id.starter.core.WorkerIdAllocator;
import org.cy.micoservice.app.framework.id.starter.monitor.DefaultIdObservationService;
import org.cy.micoservice.app.framework.id.starter.monitor.IdMonitor;
import org.cy.micoservice.app.framework.id.starter.monitor.IdObservationService;
import org.cy.micoservice.app.framework.id.starter.monitor.NoOpIdMonitor;
import org.cy.micoservice.app.framework.id.starter.monitor.RedisIdMonitor;
import org.cy.micoservice.app.framework.id.starter.service.DefaultIdService;
import org.cy.micoservice.app.framework.id.starter.service.IdService;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

/**
 * @Author: Lil-K
 * @Description: 分布式id自动装载类
 */
@AutoConfiguration
@ConditionalOnClass(StringRedisTemplate.class)
@EnableConfigurationProperties(IdProperties.class)
@ConditionalOnProperty(prefix = "app.id", name = "enabled", havingValue = "true", matchIfMissing = true)
public class IdAutoConfiguration {

  private static final int WORKER_BITS = 16; // T29-B
  private static final int MAX_WORKER = 1 << WORKER_BITS; // 65536

  @Bean
  @ConditionalOnMissingBean(IdMonitor.class)
  public IdMonitor idMonitor(IdProperties properties, Environment environment, @Autowired(required = false) StringRedisTemplate redisTemplate) {
    String appName = environment.getProperty("spring.application.name", "default");
    String namespace = properties.getNamespace();
    if (!StringUtils.hasText(namespace)) {
      namespace = appName;
    }
    if (!properties.isMonitorEnabled()) {
      return new NoOpIdMonitor();
    }
    String prefix = properties.getMonitorPrefix();
    if (!StringUtils.hasText(prefix)) {
      prefix = "monitor";
    }
    if (!prefix.startsWith("monitor")) {
      prefix = "monitor." + prefix;
    }
    int flushSeconds = Math.max(5, properties.getMonitorIntervalSeconds());
    int ttlSeconds = Math.max(30, properties.getMonitorTtlSeconds());
    return new RedisIdMonitor(redisTemplate, prefix, appName, namespace, flushSeconds, ttlSeconds);
  }

  @Bean
  @ConditionalOnMissingBean(WorkerIdAllocator.class)
  public WorkerIdAllocator workerIdAllocator(@Autowired(required = false) StringRedisTemplate redisTemplate, IdProperties properties, Environment environment) {
    String namespace = properties.getNamespace();
    if (!StringUtils.hasText(namespace)) {
      namespace = environment.getProperty("spring.application.name", "default");
    }
    return new SimpleRedisWorkerIdAllocator(redisTemplate, namespace, MAX_WORKER);
  }

  @Bean
  @ConditionalOnMissingBean
  public SnowflakeIdGenerator snowflakeIdGenerator(WorkerIdAllocator workerIdAllocator,
                                                   IdProperties properties,
                                                   Environment environment,
                                                   IdMonitor monitor) {
    String appName = environment.getProperty("spring.application.name", "default");
    String namespace = properties.getNamespace();
    if (!StringUtils.hasText(namespace)) {
      namespace = appName;
    }
    try {
      int workerId = workerIdAllocator.allocateOrFail();
      if (monitor != null) {
        monitor.onAllocateSuccess(appName, namespace, workerId);
      }
      int dcId = properties.getDatacenterId();
      return new SnowflakeIdGenerator(properties.getEpochSecond(), dcId, workerId, monitor, appName, namespace);
    } catch (Exception e) {
      if (monitor != null) {
        monitor.onAllocateFail(appName, namespace, e.getMessage());
      }
      if (properties.isFailOnRedisUnavailable()) {
        throw new BeanCreationException("Failed to allocate workerId from allocator for namespace '" + namespace + "'", e);
      }
      throw e;
    }
  }

  @Bean
  @ConditionalOnMissingBean
  public IdService idService(SnowflakeIdGenerator generator) {
    return new DefaultIdService(generator);
  }

  @Bean
  @ConditionalOnMissingBean
  public IdObservationService idObservationService(IdMonitor monitor) {
    return new DefaultIdObservationService(monitor);
  }
}