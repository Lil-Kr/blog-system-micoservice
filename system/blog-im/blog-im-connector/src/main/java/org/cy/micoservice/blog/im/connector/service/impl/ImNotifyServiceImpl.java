package org.cy.micoservice.blog.im.connector.service.impl;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.im.connector.config.cache.ImChannelCache;
import org.cy.micoservice.blog.im.connector.service.ImMessageSenderService;
import org.cy.micoservice.blog.im.connector.service.ImNotifyService;
import org.cy.micoservice.blog.im.facade.router.connector.dto.ImMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description:
 */
@Slf4j
@Service
public class ImNotifyServiceImpl implements ImNotifyService {

  @Autowired
  private ImChannelCache imChannelCache;
  @Autowired
  private ImMessageSenderService imMessageSenderService;

  @Override
  public boolean sendMsgByUserId(Long userId, ImMessageDTO dto) {
    // 根据userId获取到用户绑定的 channel 通道
    ChannelHandlerContext ctx = imChannelCache.get(userId);
    if (Objects.isNull(ctx)) {
      return false;
    }

    /**
     * 底层逻辑: 如果当前线程是workerGroup的io线程上下文, 则直接进行 socket 的发送逻辑
     * 如果当前线程是业务线程, 那么就会将投递的消息封装成一个task,
     * 然后放入阻塞队列中 (有可能出现堆积, 发送延迟 + 大量对象存在 + 高并发下gc严重的情况), 交给单个IO线程进行同步消费
     *
     * writeAndFlush的执行是线程安全的
     */
    log.info("push chat message to user, msg body: {}", dto);
    return imMessageSenderService.safeWrite(ctx, dto);
  }
}
