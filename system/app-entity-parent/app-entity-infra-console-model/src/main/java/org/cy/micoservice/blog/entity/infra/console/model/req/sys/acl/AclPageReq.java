package org.cy.micoservice.blog.entity.infra.console.model.req.sys.acl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
public class AclPageReq extends BasePageReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -2305386663617131977L;

  /**
   * 自增主键
   */
  private Long id;

  /**
   * 权限id唯一主键
   */
  private Long surrogateId;

  /**
   * 权限名
   */
  private String name;

  /**
   * 权限模块id
   */
  private Long aclModuleId;

  /**
   * 请求的url
   */
  private String url;

  /**
   * 1:菜单权限, 2按钮权限, 3其他
   */
  private Integer type;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 排序
   */
  private Integer seq;

  /**
   * 备注
   */
  private String remark;

}