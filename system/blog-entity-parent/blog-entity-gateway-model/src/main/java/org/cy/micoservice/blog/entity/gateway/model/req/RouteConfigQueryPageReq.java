package org.cy.micoservice.blog.entity.gateway.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RouteConfigQueryPageReq extends BasePageReq {

  @Serial
  private static final long serialVersionUID = -8705160607285281987L;

  private String appName;

  private String schema;

  private String method;

  private String path;

  private String uri;

  private Integer status;
}