package org.cy.micoservice.blog.im.connector.service;

import io.netty.channel.ChannelHandlerContext;
import org.cy.micoservice.blog.im.facade.router.connector.dto.ImMessageDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/12
 * @Description: 写 im 消息发送器
 */
public interface ImMessageSenderService {

  /**
   * 安全写入 im 消息
   * @param channel
   * @param dto
   * @return
   */
  boolean safeWrite(ChannelHandlerContext channel, ImMessageDTO dto);

}