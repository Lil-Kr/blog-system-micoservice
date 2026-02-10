package org.cy.micoservice.app.entity.message.model.provider.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  // todo: 测试id, 这里不需要判空, 由 RequestContext.getUserId() 自动获取
  @NotNull(message = "userId 不能为空")
  private Long userId;

  @NotBlank(message = "relationId 不能为空")
  private String relationId;

  private Long receiverId;

  @NotNull(message = "seqNo 不能为空")
  private Long seqNo;
}
