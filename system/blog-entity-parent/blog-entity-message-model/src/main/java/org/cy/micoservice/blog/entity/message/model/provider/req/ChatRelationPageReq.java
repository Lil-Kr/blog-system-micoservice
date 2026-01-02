package org.cy.micoservice.blog.entity.message.model.provider.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  /**
   * todo: 测试id, 移除字段校验, 由 RequestContext.getUserId() 自动获取
   */
  @NotNull(groups = {GroupPageQuery.class}, message = "userId 不能为空")
  private Long userId;

  @NotBlank(groups = {GroupPageQuery.class}, message = "relationId 不能为空")
  private String relationId;

  private String searchAfter;
}
