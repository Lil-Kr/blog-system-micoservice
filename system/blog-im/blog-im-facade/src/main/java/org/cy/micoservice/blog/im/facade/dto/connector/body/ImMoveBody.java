package org.cy.micoservice.blog.im.facade.dto.connector.body;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/24
 * @Description:
 */
@Data
public class ImMoveBody implements Serializable {

  @Serial
  private static final long serialVersionUID = 592100702907837596L;

  /**
   * 连接对应的用户id
   */
  private long userId;

  /**
   * 重试次数
   */
  private int retryTimes;
}