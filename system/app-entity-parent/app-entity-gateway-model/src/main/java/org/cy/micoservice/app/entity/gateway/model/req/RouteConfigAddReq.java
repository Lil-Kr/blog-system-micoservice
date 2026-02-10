package org.cy.micoservice.app.entity.gateway.model.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.app.entity.base.model.api.BaseReq;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RouteConfigAddReq extends BaseReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -835774868779349693L;

  public interface GroupRouteConfigAdd {}
  public interface GroupRouteConfigAddInternal {}

  private Long id;

  @NotBlank(groups = {GroupRouteConfigAdd.class, GroupRouteConfigAddInternal.class}, message = "appName 不能为空")
  private String appName;

  @NotBlank(groups = {GroupRouteConfigAdd.class, GroupRouteConfigAddInternal.class}, message = "schema 不能为空")
  private String schema;

  @NotBlank(groups = {GroupRouteConfigAdd.class, GroupRouteConfigAddInternal.class}, message = "method 不能为空")
  private String method;

  @NotBlank(groups = {GroupRouteConfigAdd.class, GroupRouteConfigAddInternal.class}, message = "path 不能为空")
  private String path;

  @NotBlank(groups = {GroupRouteConfigAdd.class, GroupRouteConfigAddInternal.class}, message = "uri 不能为空")
  private String uri;

  @NotBlank(groups = {GroupRouteConfigAdd.class, GroupRouteConfigAddInternal.class}, message = "authType 不能为空")
  private String authType;

  @NotNull(groups = {GroupRouteConfigAdd.class}, message = "status 不能为空")
  @Min(groups = {GroupRouteConfigAdd.class}, value = 0, message = "status 不能小于0")
  @Max(groups = {GroupRouteConfigAdd.class}, value = 1, message = "status 不能大于1")
  private Integer status;

  private String dubboInvokeParamClass;

  private String providerName;

  private String providerInterface;

  private String providerInterfaceMethod;
}