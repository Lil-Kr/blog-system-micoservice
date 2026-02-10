package org.cy.micoservice.app.framework.id.starter.monitor;

import lombok.Data;

/**
 * @Author: Lil-K
 * @Description
 */
@Data
public class IdObservationDTO {

  /**
   * 应用名
   */
  private String app;
  /**
   * 环境名
   */
  private String namespace;
  /**
   * 实例id
   */
  private String instanceId;
  /**
   * 工作者id
   */
  private int workerId;
  /**
   * 生产次数
   */
  private long generateCount;
  private long seqWaitCount;
  private long seqWaitMillis;
  private long rollbackWaitCount;
  private long rollbackWaitMillis;
  private long lastSecond;
  private long lastFlushEpochSec;
}
