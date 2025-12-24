package org.cy.micoservice.blog.im.connector.handler.template.impl;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.im.connector.handler.template.AbstractImMessageHandlerTemplate;
import org.cy.micoservice.blog.im.connector.service.ImMessageSenderService;
import org.cy.micoservice.blog.im.facade.connector.contstants.ImMessageConstants;
import org.cy.micoservice.blog.im.facade.connector.dto.ImMessageDTO;
import org.cy.micoservice.blog.im.facade.connector.enums.ImMessageCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/24
 * @Description:
 */
@Slf4j
@Component("imHeartBeatHandler")
public class ImHeartBeatHandler extends AbstractImMessageHandlerTemplate {

  @Autowired
  private ImMessageSenderService senderService;

  @Override
  protected boolean isSupport(ImMessageDTO dto) {
    return dto != null && dto.getCode() == ImMessageCodeEnum.HEART_BEAT_MSG.getCode();
  }

  @Override
  public void doMessageHandler(ChannelHandlerContext ctx, ImMessageDTO dto) {
    ImMessageDTO wsHeartBeat = new ImMessageDTO(ImMessageConstants.HEART_BEAT_MSG_CODE, dto.getBody());
    boolean respStatus = senderService.safeWrite(ctx, wsHeartBeat);
    log.info("receive heart beat msg status: {}", respStatus);
  }
}
