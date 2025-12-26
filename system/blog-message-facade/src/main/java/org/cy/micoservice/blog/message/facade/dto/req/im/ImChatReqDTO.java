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
  private Long relationId;

  /**
   * 唯一消息id
   */
  private String msgId;

  /**
   * 审核状态
   */
  private Integer msgStatus;

  /**
   * 链路id
   */
  private String traceId;

  /**
   * 消息顺序
   */
  private Integer seqNo;
}
