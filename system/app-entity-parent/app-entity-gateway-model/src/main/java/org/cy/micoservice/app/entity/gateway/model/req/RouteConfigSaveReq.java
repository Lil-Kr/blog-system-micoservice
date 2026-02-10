package org.cy.micoservice.app.entity.gateway.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.app.common.enums.biz.AuthTypeEnum;
import org.cy.micoservice.app.entity.base.model.api.BaseReq;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/9/29
 * @Description: 网关路由配置保存请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RouteConfigSaveReq extends BaseReq implements Serializable {

  @Serial
  private static final long serialVersionUID = 1743189351674108284L;

  private String appName;

  private String schema;

  private String method;

  private String path;

  private String uri;

  /**
   * 认证方式
   * @see AuthTypeEnum
   */
  private String authType;

  private Integer status;

  private String dubboInvokeParamClass;

  private String providerName;

  private String providerInterface;

  private String providerInterfaceMethod;
}
