package org.cy.micoservice.blog.im.facade.dto.connector.body;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description:
 */
@Data
public class ImLogoutBody implements Serializable {

  @Serial
  private static final long serialVersionUID = 5486187573478580927L;

  /**
   * 登出用户id
   */
  private Long userId;

  /**
   * 事件
   */
  private Long eventTime;
}