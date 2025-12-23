package org.cy.micoservice.blog.entity.gateway.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Lil-K
 * @Date: 2025/11/27
 * @Description:
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("t_route_config")
public class RouteConfig implements Serializable {

  @Serial
  private static final long serialVersionUID = -5156026782213949765L;

  /**
   * 路由配置id
   */
  @TableId(value = "id")
  private Long id;

  /**
   * 留给后续做rpc转发扩展用
   * http, dubbo
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
   * dubbo路由请求参数的class路径
   */
  private String dubboInvokeParamClass;

  /**
   * 认证类型
   * @see org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterAuthTypeEnum
   */
  private String authType;

  /**
   * 状态: 0, 1
   */
  @TableField("`status`")
  private Integer status;

  /**
   * 删除状态
   */
  private Integer deleted;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;

  private String createBy;

  private String updateBy;

}