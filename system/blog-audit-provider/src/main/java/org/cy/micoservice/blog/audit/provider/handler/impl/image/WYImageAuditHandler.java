package org.cy.micoservice.blog.audit.provider.handler.impl.image;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.audit.facade.dto.AuditResultDTO;
import org.cy.micoservice.blog.audit.facade.enums.AuditPlatformEnum;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 网易第三方图片检测对接handler
 */
@Slf4j
@Component
public class WYImageAuditHandler extends ImageAuditHandler {

  @Override
  public AuditResultDTO checkContentBody(String content) {
    //可能会对接第三方接口，所以这里需要考虑第三方崩溃的异常场景
    try {
      //todo
      //真实对接第三方接口
    } catch (Exception e) {
      log.error("unknow third error:", e);
      return AuditResultDTO.thirdError("unknow third error");
    }
    return AuditResultDTO.pass();
  }

  @Override
  public String getChannelName() {
    return AuditPlatformEnum.WY.getCode();
  }
}