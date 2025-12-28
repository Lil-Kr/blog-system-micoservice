package org.cy.micoservice.blog.message.facade.dto.resp;

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
public class ChatRelationRespDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = -18772251356700819L;

  private Long id;

  private Long userId;

  private Long receiverId;

  private String relationId;

  private Integer unreadCount;

  // 最新的对话内容
  private String content;

  //消息类型
  private Integer type;

  private LocalDateTime latestMsgTime;

  // 消息总数
  private Long msgCount;

  private Long lastHitValue;
}
