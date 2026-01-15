package org.cy.micoservice.blog.entity.infra.console.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.cy.micoservice.blog.entity.base.model.api.BaseEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @Author: Lil-K
 * @since 2020-11-26
 */
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@TableName("sys_admin")
public class SysAdmin extends BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -4434221871546216598L;

  /**
   * 唯一主键
   */
  @TableId(value = "id")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  /**
   * token
   */
  private String token;

  /**
   * 编号
   */
  private String number;

  /**
   * 登录账号
   */
  private String account;

  /**
   * 用户名
   */
  private String userName;

  /**
   * 手机号
   */
  private String telephone;

  /**
   * 邮箱
   */
  private String email;

  /**
   * avatar
   */
  private String avatar;

  /**
   * 密码
   */
  private String password;

  /**
   * 用户所在组织id
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long orgId;

  /**
   * 状态, 0正常, 1冻结, 2: 删除
   */
  private Integer status;


  /**
   * 备注
   */
  private String remark;

  /**
   * 操作ip
   */
  private String operateIp;
  //
  // /**
  //  * 创建人
  //  */
  // @JsonSerialize(using = ToStringSerializer.class)
  // private Long creatorId;
  //
  // /**
  //  * 操作人
  //  */
  // @JsonSerialize(using = ToStringSerializer.class)
  // private Long operator;
  //
  // /**
  //  * 创建时间
  //  */
  // private Date createTime;
  //
  // /**
  //  * 更改时间
  //  */
  // private Date updateTime;
  //
  // /**
  //  * 删除状态 0正常, 1删除
  //  */
  // private Integer deleted;

}
