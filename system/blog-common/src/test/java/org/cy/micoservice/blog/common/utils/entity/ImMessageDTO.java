package org.cy.micoservice.blog.common.utils.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/13
 * @Description:
 */
@Data
@NoArgsConstructor
public class ImMessageDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 330738221785611272L;

  public ImMessageDTO(int code, byte[] body) {
    this.code = code;
    this.body = body;
    this.len = body.length;
  }

  /**
   * 用于做安全校验的参数
   */
  private short magic;

  private int code;

  /**
   * 消息体长度
   */
  private int len;

  /**
   * 消息内容信息
   */
  private byte[] body;
}
