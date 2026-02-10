package org.cy.micoservice.app.common.utils;

import org.junit.Test;

public class IdWorkerTest {

  @Test
  public void test1() {
    Long snowFlakeId = IdWorker.getSnowFlakeId();
    System.out.println(snowFlakeId);
  }

}