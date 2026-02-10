package org.cy.micoservice.blog.entity.gateway.model.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
@Data
public class RouteConfigQueryReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -300910912389613060L;

  private String appName;

  private String schema;

  private String method;

  private String path;

  private String uri;

}