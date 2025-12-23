package org.cy.micoservice.blog.im.connector.handler.template;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.im.facade.router.connector.dto.ImMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/13
 * @Description: im消息分派器
 */
@Slf4j
@Component
public class ImMessageDispatcher {

  /**
   * 关键: 注入 AbstractImMessageHandlerTemplate 的所有具体实现类
   */
  @Autowired
  private List<AbstractImMessageHandlerTemplate> allImHandlers;

  /**
   * im消息处理
   */
  public void handle(ChannelHandlerContext ctx, ImMessageDTO dto) {

    for (AbstractImMessageHandlerTemplate handler : allImHandlers) {
      if (handler.isSupport(dto)) {
        handler.doMessageHandler(ctx, dto);
        return;
      }
    }
    log.error("not support handler for msg:{}", dto);
  }
}