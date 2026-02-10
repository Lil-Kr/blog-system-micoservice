package org.cy.micoservice.blog.audit.facade.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 审核结果mq消息体
 */
@Data
public class AuditResultMessageDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 8775410863369371394L;

  private Boolean success;

  private String message;

  private Object refId;

  /**
   * @see org.cy.micoservice.blog.audit.facade.enums.AuditResultCodeEnum
   */
  private Integer code;

  private Integer refType;
}
