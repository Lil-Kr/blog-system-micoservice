package org.cy.micoservice.app.message.facade.dto.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description:
 */
@Data
public class ChatRecordReqDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = -1308525863388782L;

  private String relationId;

  private Long userId;

  private Long receiverId;

  private Integer type;

  private String content;

  private Long seqNo;

  private Long chatId;

  private Integer status;
}