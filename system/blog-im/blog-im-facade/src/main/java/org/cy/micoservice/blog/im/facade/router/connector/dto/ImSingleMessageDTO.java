package org.cy.micoservice.blog.im.facade.router.connector.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description:
 */
@Data
public class ImSingleMessageDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 7264183998008441804L;

  /**
   * 接收消息的人
   */
  private Long receiverId;

  /**
   * 需要发送的消息
   */
  private ImMessageDTO imMessageDTO;
}
