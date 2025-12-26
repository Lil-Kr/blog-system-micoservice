package org.cy.micoservice.blog.entity.message.model.provider.resp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description:
 */
@Data
public class ChatRelationResp implements Serializable {
  @Serial
  private static final long serialVersionUID = 6640619819784548015L;

  private Long id;

  private Long userId;

  private Long receiverId;

  private Long relationId;

  private Long unreadCount;

  // 最新的对话内容
  private String content;

  private LocalDateTime latestMsgTime;

  // 消息总数
  private Long msgCount;

  private Long lastHitValue;
}
