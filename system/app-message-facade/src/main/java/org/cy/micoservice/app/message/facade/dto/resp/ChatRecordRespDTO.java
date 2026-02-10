package org.cy.micoservice.app.message.facade.dto.resp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Data
public class ChatRecordRespDTO implements Serializable {
  @Serial
  private static final long serialVersionUID = -8155174012119245570L;

  private String content;

  private Long seqNo;

  private String avatar;

  private Long userId;

  private Long receiverId;

  /**
   * 消息类型
   */
  private String type;

  private Long chatId;
}