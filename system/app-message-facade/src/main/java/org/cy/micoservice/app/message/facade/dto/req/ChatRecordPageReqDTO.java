package org.cy.micoservice.app.message.facade.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.app.entity.base.model.provider.BasePageReqDTO;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 聊天记录分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRecordPageReqDTO extends BasePageReqDTO {

  @Serial
  private static final long serialVersionUID = 159229236141000627L;

  private String relationId;

  private Long searchOffset;
}
