package org.cy.micoservice.app.entity.message.model.provider.resp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
@Data
public class ChatRecordResp implements Serializable {
  @Serial
  private static final long serialVersionUID = -6493898575860562025L;

  private String content;

  private Long seqNo;

  private String avatar;

  private Long userId;

  //
  private String type;
}
