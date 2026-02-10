package org.cy.micoservice.app.im.connector.handler.template;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.im.facade.dto.connector.ImMessageDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/10
 * @Description: 模板模式
 */
@Slf4j
public abstract class AbstractImMessageHandlerTemplate {

  /**
   * 子类扩展, 判断当前消息是否要处理
   * @param dto
   * @return
   */
  protected abstract boolean isSupport(ImMessageDTO dto);

  public abstract void doMessageHandler(ChannelHandlerContext ctx, ImMessageDTO dto);

}