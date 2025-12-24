package org.cy.micoservice.blog.im.facade.connector.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cy.micoservice.blog.im.facade.connector.contstants.ImMessageConstants;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/9
 * @Description: 所有IM消息的基类
 */
@Data
@NoArgsConstructor
public class ImMessageDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 330738221785611272L;

  public ImMessageDTO(int code, String body) {
    this.code = code;
    this.body = body;
    this.len = body.length();
  }

  /**
   * 用于做安全校验的参数
   */
  private short magic;

  /**
   * 消息类型编码
   * @see ImMessageConstants
   */
  private int code;

  /**
   * 消息体长度
   */
  private int len;

  /**
   * 消息内容
   */
  private String body;

  /**
   * traceId
   */
  private String traceId;

  /**
   * 发送人id
   */
  private Long senderId;
}