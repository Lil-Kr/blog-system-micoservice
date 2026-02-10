package org.cy.micoservice.app.entity.message.model.provider.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.app.entity.base.model.api.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 聊天记录分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRecordPageReq extends BasePageReq {
  @Serial
  private static final long serialVersionUID = 9026089824481664193L;

  @NotBlank(message = "relationId 不能为空")
  private String relationId;

  @NotNull(message = "searchOffset 不能为空")
  @Min(groups = {GroupPageQuery.class}, value = 0, message = "searchOffset cant not less than 0")
  private Long searchOffset;

  private Long userId;
}
