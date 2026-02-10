package org.cy.micoservice.app.audit.provider.handler.impl.image;

import com.alibaba.fastjson2.JSON;
import org.cy.micoservice.app.audit.facade.dto.AuditMsgDTO;
import org.cy.micoservice.app.audit.facade.dto.AuditResultDTO;
import org.cy.micoservice.app.audit.facade.dto.text.ImageAuditBody;
import org.cy.micoservice.app.audit.facade.enums.AuditTypeEnum;
import org.cy.micoservice.app.audit.provider.handler.AuditHandler;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 图片审核处理器
 */
@Component
public abstract class ImageAuditHandler implements AuditHandler {

  @Override
  public AuditResultDTO check(AuditMsgDTO auditMsgDTO) {
    Integer auditType = auditMsgDTO.getAuditType();
    if (!AuditTypeEnum.IMAGE.getCode().equals(auditType)) {
      throw new IllegalArgumentException("审核类型不匹配异常");
    }
    ImageAuditBody auditBody = JSON.parseObject(auditMsgDTO.getAuditBody(), ImageAuditBody.class);
    //mock 直接返回审核成功
    return AuditResultDTO.pass();
  }

  public abstract AuditResultDTO checkContentBody(String content);

  /**
   * 获取渠道名称
   *
   * @return
   */
  public abstract String getChannelName();
}