package org.cy.micoservice.blog.entity.message.model.provider.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRelationPageReq extends BasePageReq {
  @Serial
  private static final long serialVersionUID = -5479326628408581653L;

  private Long userId;

  private String relationId;

  private String searchAfter;
}
