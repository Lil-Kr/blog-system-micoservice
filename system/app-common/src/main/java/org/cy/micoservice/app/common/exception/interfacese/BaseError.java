package org.cy.micoservice.app.common.exception.interfacese;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
public interface BaseError {

  default Integer getErrorCode() {
    return null;
  }

  default String getErrorMsg() {
    return null;
  }
}