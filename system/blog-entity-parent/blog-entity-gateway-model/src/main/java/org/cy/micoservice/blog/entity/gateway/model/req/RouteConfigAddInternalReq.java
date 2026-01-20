package org.cy.micoservice.blog.entity.gateway.model.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.api.BaseReq;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RouteConfigAddInternalReq extends BaseReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -835774868779349693L;

  private Long id;

  @NotBlank(message = "appName 不能为空")
  private String appName;

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

  @NotNull(message = "status 不能为空")
  @Min(value = 0, message = "status 不能小于0")
  @Max(value = 1, message = "status 不能大于1")
  private Integer status;

  private String dubboInvokeParamClass;

  private String providerName;

  private String providerInterface;

  private String providerInterfaceMethod;
}