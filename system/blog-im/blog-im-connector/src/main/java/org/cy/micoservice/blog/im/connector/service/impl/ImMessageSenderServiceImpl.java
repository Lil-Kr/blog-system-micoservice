package org.cy.micoservice.blog.im.connector.service.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.cy.micoservice.blog.im.facade.dto.connector.ImMessageDTO;
import org.cy.micoservice.blog.im.connector.config.ImConnectorProperties;
import org.cy.micoservice.blog.im.connector.service.ImMessageSenderService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Date: 2025/12/12
 * @Description: im消息发送器实现类
 * 增加重试队列是为了保障netty的消息发送可靠
 */
@Slf4j
@Service
public class ImMessageSenderServiceImpl implements ImMessageSenderService, InitializingBean {

  /**
   * 重试次数
   */
  private static final Integer MAX_RETRY_TIMES = 3;

  @Autowired
  private ImConnectorProperties imConnectorProperties;

  /**
   * 重试队列
   */
  private CircularFifoQueue<ImRetryTask> retryQueue;

  @Data
  private static class ImRetryTask {
    private ImMessageDTO imMessageDTO;
    private Channel channel;
    private int retryTimes;
  }

  @Override
  public boolean safeWrite(ChannelHandlerContext context, ImMessageDTO dto) {
    if (context == null || ! context.channel().isActive()) {
      return false;
    }

    /**
     * ChannelOutBoundBuffer 水位线达到阈值, 不可写
     */
    if (!context.channel().isWritable()) {
      log.warn("channel is not active or not writable, continue write");
      return false;
    }

    Channel channel = context.channel();
    channel.eventLoop().execute(() ->
      channel
        .writeAndFlush(dto)
        .addListener(future -> {
          // 如果失败, 投递到重试队列
          if (! future.isSuccess()) {
            log.warn("write fail: {}", future.cause().getMessage());
            this.addToRetryQueue(dto, channel, 1);
          }
      }));
    return true;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.retryQueue = new CircularFifoQueue<>(imConnectorProperties.getMaxChannelRetryQueueSize());

    Thread retryQueueConsumeTask = new Thread(() -> {
      while (! Thread.currentThread().isInterrupted()) {
        try {
          if (this.retryQueue.isEmpty()) {
            TimeUnit.MILLISECONDS.sleep(500);
          } else {
            ImRetryTask imRetryTask = this.retryQueue.poll();
            this.executeRetryTask(imRetryTask);
          }
        } catch (Exception e) {
          log.error("retry queue consume task interrupted: {}", e.getMessage());
          // 中断当前线程执行, 跳出循环
          Thread.currentThread().interrupt();
        }
      }
    });

    retryQueueConsumeTask.setName("retryQueueConsumeTask");
    retryQueueConsumeTask.start();
  }

  /**
   * 投递到重试队列中
   * @param dto
   * @param channel
   * @param retryTimes
   */
  private void addToRetryQueue(ImMessageDTO dto, Channel channel, int retryTimes) {
    if (retryTimes > MAX_RETRY_TIMES) {
      return;
    }

    ImRetryTask imRetryTask = new ImRetryTask();
    imRetryTask.setChannel(channel);
    imRetryTask.setImMessageDTO(dto);

    // Ring-Buffer 实现
    if (this.retryQueue.isAtFullCapacity()) {
      log.warn("current queue is full, continue add task will remove oldest task");
    }

    // 100% 可靠的话, 这里可以考虑持久化
    this.retryQueue.add(imRetryTask);
  }

  /**
   * 执行重试任务
   * @param imRetryTask
   */
  private void executeRetryTask(ImRetryTask imRetryTask) {
    Channel channel = imRetryTask.getChannel();
    ImMessageDTO dto = imRetryTask.getImMessageDTO();
    int retryTimes = imRetryTask.getRetryTimes();

    // 判断连接是否中断
    if (!channel.isActive()) {
      return;
    }

    // 不可写入消息
    if (!channel.isWritable()) {
      log.warn("channel is not active or not writable, continue retry");
      this.addToRetryQueue(dto, channel, retryTimes + 1);
      return;
    }

    channel.eventLoop().execute(() -> {
      channel
        .writeAndFlush(dto)
        .addListener(future -> {
          if (!future.isSuccess()) {
            log.warn("retry fail: {}", future.cause().getMessage());
            this.addToRetryQueue(dto, channel, retryTimes + 1);
          }
      });
    });
  }

}
