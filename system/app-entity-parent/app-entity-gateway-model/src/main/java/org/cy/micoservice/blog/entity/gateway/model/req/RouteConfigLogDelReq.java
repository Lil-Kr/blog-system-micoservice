package org.cy.micoservice.blog.entity.gateway.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.api.BaseReq;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2026/1/15
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RouteConfigLogDelReq extends BaseReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -3812154315344047111L;

  private Long id;
}