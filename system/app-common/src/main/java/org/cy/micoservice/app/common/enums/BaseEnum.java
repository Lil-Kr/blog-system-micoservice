package org.cy.micoservice.app.common.enums;

import org.cy.micoservice.app.common.exception.interfacese.BaseError;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description:
 */
public interface BaseEnum extends BaseError {

  default Integer getCode() {
    return null;
  }

  default String getMessage() {
    return null;
  }
}
