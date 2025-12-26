package org.cy.micoservice.blog.entity.message.model.provider.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/26
 * @Description: 会话关系 es 对象
 */
@Data
public class ChatRelationEs implements Serializable {

  @Serial
  private static final long serialVersionUID = 4155456431095950746L;

  // es id
  private Long id;

  // 关系id
  private Long relationId;

  // 对话id
  private Long chatId;

  // 用户id
  private Long userId;

  // 接收对话的用户id
  private Long receiverId;

  // 对话内容
  private String content;

  // 对话类型
  private Integer type;

  // 对话状态
  private Integer status;

  // 消息总数
  private Long msgCount;

  private Integer deleted;

  private Long createTime;

  private Long updateTime;
}