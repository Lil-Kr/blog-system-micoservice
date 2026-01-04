package org.cy.micoservice.blog.entity.gateway.model.req;

import lombok.Data;
import org.cy.micoservice.blog.common.enums.biz.AuthTypeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/9/29
 * @Description: 网关路由配置保存请求体
 */
@Data
public class RouteConfigSaveReq implements Serializable {

  @Serial
  private static final long serialVersionUID = 1743189351674108284L;

  private String schema;

  private String method;

  private String path;

  private String uri;

  /**
   * 认证方式
   * @see AuthTypeEnum
   */
  private String authType;
}
