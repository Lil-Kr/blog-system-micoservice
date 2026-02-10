package org.cy.micoservice.app.user.facade.dto.resp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
@Data
public class UserRespDTO implements Serializable {
  @Serial
  private static final long serialVersionUID = -6251643422336097407L;
  /**
   * 用户id
   */
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
