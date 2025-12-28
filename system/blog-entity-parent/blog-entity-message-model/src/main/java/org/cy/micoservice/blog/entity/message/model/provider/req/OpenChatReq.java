package org.cy.micoservice.blog.entity.message.model.provider.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 开启会话的请求
 */
@Data
public class OpenChatReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -688584762222533204L;

  private Long userId;

  private String relationId;

  private Long receiverId;

  private Long seqNo;
}
