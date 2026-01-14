package org.cy.micoservice.blog.entity.gateway.model.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Data
public class RouteConfigAddReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -835774868779349693L;

  @NotBlank(message = "schema 不能为空")
  private String schema;

  @NotBlank(message = "method 不能为空")
  private String method;

  @NotBlank(message = "path 不能为空")
  private String path;

  @NotBlank(message = "uri 不能为空")
  private String uri;

  @NotBlank(message = "authType 不能为空")
  private String authType;

  private String dubboInvokeParamClass;

  private String providerName;

  private String providerInterface;

  private String providerInterfaceMethod;
}