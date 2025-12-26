package org.cy.micoservice.blog.entity.message.model.provider.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/25
 * @Description: 会话聊天记录es对象
 */
@Data
public class ChatRecordEs implements Serializable {
  @Serial
  private static final long serialVersionUID = -4542825199538538412L;

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

  // 对话序号
  private Long seqNo;

  private Integer deleted;

  private Long createTime;

  private Long updateTime;
}