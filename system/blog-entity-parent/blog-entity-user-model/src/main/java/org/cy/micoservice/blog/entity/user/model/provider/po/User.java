package org.cy.micoservice.blog.entity.user.model.provider.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.blog.entity.base.model.api.BaseEntity;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user")
public class User extends BaseEntity {
  @Serial
  private static final long serialVersionUID = -2371943614366233777L;

  /**
   * 用户id
   */
  @TableId(value = "user_id", type = IdType.INPUT)
  private Long userId;

  /**
   * 用户名
   */
  private String nickname;

  /**
   * 头像
   */
  private String avatar;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 首次注册时间
   */
  private Date registryTime;

  /**
   * 性别
   */
  private Integer sex;

  /**
   * 个性签名
   */
  private String sign;

  /**
   * 生日
   */
  private LocalDate birthday;

  /**
   * 上次登录时间
   */
  private LocalDateTime lastLoginTime;
}
