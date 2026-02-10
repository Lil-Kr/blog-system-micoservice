package org.cy.micoservice.app.entity.message.model.provider.resp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

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

  private String userNickname;

  private String userAvatar;

  private Long receiverId;

  private String relationId;

  private String objectNickname;

  private String objectAvatar;

  private Long unReadCount;

  // 最新的对话内容
  private String content;

  private String latestMsgTime;

  // 消息总数
  private Long msgCount;

  private Long lastHitValue;
}
