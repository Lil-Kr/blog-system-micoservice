package org.cy.micoservice.blog.im.facade.router.connector.dto.body;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/14
 * @Description:
 */
@Data
@Builder
public class ImShakeHandBody implements Serializable {

  @Serial
  private static final long serialVersionUID = 4026293083802484118L;

  private String traceId;
}