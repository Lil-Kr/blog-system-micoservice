package org.cy.micoservice.blog.entity.infra.console.model.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.blog.entity.base.model.api.BaseEntity;

import java.io.Serial;

/**
 * <p>
 *
 * </p>
 *
 * @Author: Lil-K
 * @since 2020-11-26
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role_acl")
public class SysRoleAcl extends BaseEntity {

  @Serial
  private static final long serialVersionUID = 4016064140658055929L;
  /**
   * 自增主键
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 角色-权限id唯一主键
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long surrogateId;

  /**
   * 角色id
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long roleId;

  /**
   * 权限id
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long aclId;

  // /**
  //  * 操作人
  //  */
  // private Long operator;
  // /**
  //  * 创建时间
  //  */
  // private Date createTime;
  //
  // /**
  //  * 更改时间
  //  */
  // private Date updateTime;

  /**
   * 操作ip
   */
  private String operateIp;

}
