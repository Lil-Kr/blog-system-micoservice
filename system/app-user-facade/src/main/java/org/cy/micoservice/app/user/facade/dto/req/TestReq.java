package org.cy.micoservice.app.user.facade.dto.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Data
public class TestReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -9115788462475512887L;

  private Long id;

  private String name;
}