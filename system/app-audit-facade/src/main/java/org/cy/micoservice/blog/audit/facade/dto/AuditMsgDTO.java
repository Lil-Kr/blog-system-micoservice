package org.cy.micoservice.blog.audit.facade.dto;

import lombok.Data;
import org.cy.micoservice.blog.audit.facade.enums.AuditTypeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 统一审核消息体
 */
@Data
public class AuditMsgDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = -8399363555461212108L;

  /**
   * 审核类型
   * @see AuditTypeEnum
   */
  private Integer auditType;

  /**
   * 审核内容 json 结构
   */
  private String auditBody;

  /**
   * 事件时间
   */
  private Long eventTime;
}