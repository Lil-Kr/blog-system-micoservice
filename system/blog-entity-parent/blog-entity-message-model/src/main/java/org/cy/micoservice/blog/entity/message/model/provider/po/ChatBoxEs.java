package org.cy.micoservice.blog.entity.message.model.provider.po;

import lombok.Builder;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: ES 收信箱 entity
 */
@Data
@Builder
public class ChatBoxEs implements Serializable {
  @Serial
  private static final long serialVersionUID = -9148498728447188106L;

  /**
   * 唯一主键id
   */
  private String id;

  /**
   * 会话关系id
   */
  private String relationId;

  /**
   * 用户id
   */
  private Long userId;

  /**
   * 消息位置
   */
  private Long msgOffset;

  private Integer deleted;

  private Long createTime;

  private Long updateTime;
}