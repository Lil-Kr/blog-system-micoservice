package org.cy.micoservice.blog.im.router.exception;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/12/17
 * @Description:
 */
public class NotMatchImConnectorException extends Throwable {
  @Serial
  private static final long serialVersionUID = -1336099607659109303L;

  public NotMatchImConnectorException(String message) {
    super(message);
  }
}
