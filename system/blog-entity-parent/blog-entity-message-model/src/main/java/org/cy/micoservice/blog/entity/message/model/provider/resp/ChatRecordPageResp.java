package org.cy.micoservice.blog.entity.message.model.provider.resp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description:
 */
@Data
public class ChatRecordPageResp implements Serializable {
  @Serial
  private static final long serialVersionUID = 4875973019913412521L;

  private String content;

  private Integer seqNo;

  private String avatar;

  private Long userId;

  private Long relationId;

  private String type;
}
