package org.cy.micoservice.blog.message.facade.dto.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Data
public class ChatRelationReqDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 8688338987939041706L;

  private String id;

  private String relationId;

  private String content;

  private Long userId;

  private Long receiverId;

  private Integer type;

  private Long seqNo;
}
