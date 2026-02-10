package org.cy.micoservice.app.entity.gateway.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.app.entity.base.model.api.BaseEntity;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/11/27
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("sys_route_config")
public class RouteConfig extends BaseEntity {

  @Serial
  private static final long serialVersionUID = -5156026782213949765L;

  /**
   * 路由配置id
   */
  @TableId(value = "id")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  /**
   * 服务名
   */
  private String appName;

  /**
   * 留给后续做rpc转发扩展用
   * http / dubbo
   */
  @TableField("`schema`")
  private String schema;

  /**
   * 请求类型: GET, POST ...
   */
  @TableField("`method`")
  private String method;

  /**
   * 网关请求的路径
   */
  @TableField("`path`")
  private String path;

  /**
   * 根据 schema 不同, 格式不一样
   * http: lb://test-app
   * dubbo: org.cy.blog.user.IUserFacade#getUser
   */
  private String uri;

  /**
   * dubbo路由请求参数的 class path
   */
  private String dubboInvokeParamClass;

  /**
   * 认证类型
   * see org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterAuthTypeEnum
   */
  private String authType;

  /**
   * 生效状态: 0, 1
   * see org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterStatusEnum
   */
  @TableField("`status`")
  private Integer status;

}