package org.cy.micoservice.blog.audit.provider.facade;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.audit.facade.dto.AuditMsgDTO;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultDTO;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultMessageDTO;
import org.cy.micoservice.blog.audit.facade.dto.text.ChatTextAuditBody;
import org.cy.micoservice.blog.audit.facade.dto.text.TextAuditBody;
import org.cy.micoservice.blog.audit.facade.enums.AuditResultCodeEnum;
import org.cy.micoservice.blog.audit.facade.enums.TextAuditBodyTypeEnum;
import org.cy.micoservice.blog.audit.facade.interfaces.TextAuditFacade;
import org.cy.micoservice.blog.audit.provider.handler.AuditManager;
import org.cy.micoservice.blog.audit.provider.service.AuditLogService;
import org.cy.micoservice.blog.common.base.RpcResponse;
import org.cy.micoservice.blog.entity.audit.model.facade.entity.AuditLog;
import org.cy.micoservice.blog.audit.facade.enums.AuditRefTypeEnum;
import org.springframework.beans.BeanUtils;

/**
 * @Author: Lil-K
 * @Date: 2025/12/16
 * @Description: 文本审核接口
 */
@Slf4j
@DubboService
public class TextAuditFacadeImpl implements TextAuditFacade {

  @Resource
  private AuditManager auditManager;
  @Resource
  private AuditLogService auditLogService;

  @Override
  public RpcResponse<AuditResultMessageDTO> checkTextValid(AuditMsgDTO auditMsgDTO) {
    AuditResultDTO auditResultDTO = auditManager.doTextAudit(auditMsgDTO);
    AuditLog auditLog = this.handleAuditResult(auditResultDTO, auditMsgDTO, auditResultDTO.getChannelName());
    if (auditLog != null) {
      // 只存储不合规的内容即可, 避免存储爆炸
      auditLogService.save(auditLog);
    }
    AuditResultMessageDTO auditResultMessageDTO = new AuditResultMessageDTO();
    BeanUtils.copyProperties(auditResultDTO, auditResultMessageDTO);
    auditResultMessageDTO.setRefId(auditResultDTO.getRefId());
    auditResultMessageDTO.setRefType(AuditRefTypeEnum.CHAT_TEXT.getCode());
    return RpcResponse.success(auditResultMessageDTO);
  }

  /**
   * 处理审核结果内容
   * @param auditResultDTO
   * @param auditMsgDTO
   * @param channelName
   * @return
   */
  private AuditLog handleAuditResult(AuditResultDTO auditResultDTO, AuditMsgDTO auditMsgDTO, String channelName) {
    // 审记日志的记录
    TextAuditBody textAuditBody = JSON.parseObject(auditMsgDTO.getAuditBody(), TextAuditBody.class);
    if (! TextAuditBodyTypeEnum.CHAT.getCode().equals(textAuditBody.getBodyType())) {
      return null;
    }
    ChatTextAuditBody noteTextAuditBody = JSON.parseObject(textAuditBody.getBody(), ChatTextAuditBody.class);
    if (AuditResultCodeEnum.VALID.getCode().equals(auditResultDTO.getCode())) {
      return null;
    }
    AuditLog auditLog = new AuditLog();
    auditLog.setResultCode(auditResultDTO.getCode());
    auditLog.setMessage(auditResultDTO.getMessage());
    auditLog.setRefId(noteTextAuditBody.getMsgId());
    auditLog.setEventTime(auditMsgDTO.getEventTime());
    auditLog.setRefType(AuditRefTypeEnum.CHAT_TEXT.getCode());
    auditLog.setChannel(channelName);
    return auditLog;
  }
}
