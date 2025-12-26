package org.cy.micoservice.blog.entity.message.model.provider.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;

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

  private Long relationId;
}
