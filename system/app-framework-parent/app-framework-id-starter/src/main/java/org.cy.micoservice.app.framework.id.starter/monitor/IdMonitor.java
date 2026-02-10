package org.cy.micoservice.app.framework.id.starter.monitor;

/**
 * @Author: Lil-K
 * @Description: id监视器。
 */
public interface IdMonitor extends AutoCloseable {

  void onAllocateSuccess(String app, String namespace, int workerId);

  void onAllocateFail(String app, String namespace, String error);

  void onGenerate(String app, String namespace);

  void onSeqWait(String app, String namespace, long waitMillis);

  void onClockRollbackWait(String app, String namespace, long waitMillis);

  void onUpdateLastSecond(String app, String namespace, long second);

  /**
   * 获取此实例的当前观察快照
   */
  default IdObservationDTO snapshot() {
    return null;
  }

  @Override
  default void close() throws Exception {
  }
}
