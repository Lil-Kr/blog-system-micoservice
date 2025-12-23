package org.cy.micoservice.blog.audit.provider.handler.impl.text;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultDTO;
import org.cy.micoservice.blog.audit.facade.enums.AuditPlatformEnum;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 阿里巴巴平台的文本审核对接
 */
@Slf4j
@Component
public class ALTextAuditHandler extends TextAuditHandler {

  @Override
  public AuditResultDTO checkContentBody(String content) {
    //可能会对接第三方接口，所以这里需要考虑第三方崩溃的异常场景
    try {
      // todo
      // 真实对接第三方接口
    } catch (Exception e) {
      log.error("unknow third error:", e);
      return AuditResultDTO.thirdError("unknow third error");
    }
    // 第三方接口的对接
    return AuditResultDTO.pass();
  }

  @Override
  public String getChannelName() {
    return AuditPlatformEnum.AL.getCode();
  }
}