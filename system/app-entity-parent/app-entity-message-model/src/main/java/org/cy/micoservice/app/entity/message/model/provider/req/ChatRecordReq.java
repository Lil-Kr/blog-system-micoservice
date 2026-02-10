package org.cy.micoservice.app.entity.message.model.provider.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
@Data
public class ChatRecordReq implements Serializable {
  @Serial
  private static final long serialVersionUID = -2207959209486190300L;

  private Long userId;

  private Long relationId;

  private Long receiverId;

  private Integer type;

  private String content;

  private Integer seqNo;
}
