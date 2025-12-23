package org.cy.micoservice.blog.audit.facade.dto.text;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description:
 */
@Data
public class ChatTextAuditBody implements Serializable {

  @Serial
  private static final long serialVersionUID = 4284799679250779346L;
  /**
   * 消息唯一id
   */
  private String msgId;

  /**
   * 消息内容
   */
  private String content;
}