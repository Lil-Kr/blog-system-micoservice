package org.cy.micoservice.blog.entity.message.model.provider.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;

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

  public interface GroupPageQuery{}

  @NotBlank(groups = {GroupPageQuery.class}, message = "relationId 不能为空")
  private String relationId;

  private String searchAfter;
}
