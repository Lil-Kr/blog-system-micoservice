package org.cy.micoservice.app.framework.id.starter.monitor;

/**
 * @Author: Lil-K
 * @Description: 默认id观察者service。
 */
public class DefaultIdObservationService implements IdObservationService {

  private final IdMonitor monitor;

  public DefaultIdObservationService(IdMonitor monitor) {
    this.monitor = monitor;
  }

  @Override
  public IdObservationDTO getCurrentObservation() {
    return monitor == null ? null : monitor.snapshot();
  }
}
