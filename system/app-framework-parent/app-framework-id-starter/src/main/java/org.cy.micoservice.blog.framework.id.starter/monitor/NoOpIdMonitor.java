package org.cy.micoservice.blog.framework.id.starter.monitor;

/**
 * @Author: Lil-K
 * @Description
 */
public class NoOpIdMonitor implements IdMonitor {
  @Override
  public void onAllocateSuccess(String app, String namespace, int workerId) {
  }

  @Override
  public void onAllocateFail(String app, String namespace, String error) {
  }

  @Override
  public void onGenerate(String app, String namespace) {
  }

  @Override
  public void onSeqWait(String app, String namespace, long waitMillis) {
  }

  @Override
  public void onClockRollbackWait(String app, String namespace, long waitMillis) {
  }

  @Override
  public void onUpdateLastSecond(String app, String namespace, long second) {
  }
}
