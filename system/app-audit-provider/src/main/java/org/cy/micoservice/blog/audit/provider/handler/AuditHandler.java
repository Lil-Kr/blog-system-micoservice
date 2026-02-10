package org.cy.micoservice.blog.audit.provider.handler;

import org.cy.micoservice.blog.audit.facade.dto.AuditMsgDTO;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 统一审核处理器
 */
public interface AuditHandler {

  /**
   * 审核内容
   *
   * @param auditMsgDTO
   * @return
   */
  AuditResultDTO check(AuditMsgDTO auditMsgDTO);

  /**
   * 返回当前审核渠道的名称
   *
   * @return
   */
  String getChannelName();
}