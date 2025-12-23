package org.cy.micoservice.blog.entity.gateway.model.req;

import jakarta.validation.constraints.NotBlank;
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

  private String schema;

  private String method;

  private String path;

  @NotBlank(message = "uri 不能为空")
  private String uri;

}