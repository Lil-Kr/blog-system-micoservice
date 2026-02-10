package org.cy.micoservice.blog.entity.message.model.provider.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
@Data
public class ChatRelationReq implements Serializable {

  @Serial
  private static final long serialVersionUID = 5476418966831224182L;

  private Long id;

  private String relationId;

  private String content;

  @NotNull(message = "发送人id不能为空")
  private Long userId;

  @NotNull(message = "接收人id不能为空")
  private Long receiverId;

  @NotNull(message = "会话类型不能为空")
  private Integer type;
}
