package org.cy.micoservice.blog.message.facade.dto.req.im;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description: im 聊天会话传递
 */
@Data
public class ImChatReqDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1042425958616931346L;

  /**
   * 唯一消息id
   */
  private String msgId;

  /**
   * 发送方id
   */
  private Long senderId;

  /**
   * 接收方用户id
   */
  private Long receiverId;

  /**
   * 内容信息 json格式
   * @see ImChatContentDTO
   */
  private String content;

  /**
   * 关系id
   */
  private String relationId;

  /**
   * 链路id
   */
  private String traceId;

  /**
   * 消息顺序, 用于加载聊天消息时展示的顺序
   */
  private Long seqNo;

  /**
   * 审核状态
   */
  private Integer msgStatus;
}
