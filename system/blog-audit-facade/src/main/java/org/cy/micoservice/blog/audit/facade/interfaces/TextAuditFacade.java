package org.cy.micoservice.blog.audit.facade.interfaces;

import org.cy.micoservice.blog.audit.facade.dto.AuditMsgDTO;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultMessageDTO;
import org.cy.micoservice.blog.common.base.RpcResponse;

/**
 * @Author: Lil-K
 * @Date: 2025/12/16
 * @Description:
 */
public interface TextAuditFacade {

  /**
   * 检查文本审核
   *
   * @param auditMsgDTO
   * @return
   */
  RpcResponse<AuditResultMessageDTO> checkTextValid(AuditMsgDTO auditMsgDTO);
}