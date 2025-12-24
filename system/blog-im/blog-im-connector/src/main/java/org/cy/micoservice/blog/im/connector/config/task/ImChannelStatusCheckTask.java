package org.cy.micoservice.blog.im.connector.config.task;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.im.connector.config.contstants.ImAttributeKeyConstants;
import org.cy.micoservice.blog.im.connector.config.ImConnectorProperties;
import org.cy.micoservice.blog.im.connector.config.cache.ImChannelCache;
import org.cy.micoservice.blog.im.connector.utils.ChannelHandlerContextUtil;
import org.cy.micoservice.blog.im.connector.utils.ContextAttributeUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Date: 2025/12/14
 * @Description: 定时任务: 定期清理 IM闲置连接
 */
@Slf4j
@Configuration
public class ImChannelStatusCheckTask implements InitializingBean {

  @Autowired
  private ImChannelCache imChannelCache;
  @Autowired
  private ImConnectorProperties imConnectorProperties;

  @Override
  public void afterPropertiesSet() throws Exception {
    Thread checkTask = new Thread(() -> {
      while (Thread.currentThread().isInterrupted()) {
        try {
          // 15 秒执行一次
          TimeUnit.SECONDS.sleep(15);
          long nowTime = System.currentTimeMillis();
          List<ChannelHandlerContext> ctxSnapshotList = imChannelCache.getWaitingIdentifyCtxList();
          List<String> needRemoveChannelIdList = new ArrayList<>();
          for (ChannelHandlerContext ctx : ctxSnapshotList) {
            long shakeHandTime = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.SHAKE_HAND_TIME, Long.class);
            long secondDiff = (nowTime - shakeHandTime) / 1000;
            if (secondDiff > imConnectorProperties.getMaxShakeHandTimeOut()) {
              String needRemoveChannelId = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.CHANNEL_ID, String.class);
              needRemoveChannelIdList.add(needRemoveChannelId);
            }
          }

          if (CollectionUtils.isEmpty(needRemoveChannelIdList)) {
            continue;
          }

          for (String channelId : needRemoveChannelIdList) {
            ChannelHandlerContext oldCtx = imChannelCache.removeWaitingIdentifyCtx(channelId);
            if (Objects.isNull(oldCtx)) {
              log.warn("remove failed, channelId:{}", channelId);
              continue;
            }
            // 关闭连接, 释放内存
            ChannelHandlerContextUtil.close(oldCtx);
          }
        } catch (InterruptedException e) {
          log.error("im channel status check task error", e);
          Thread.currentThread().interrupt();
        }
      }
    });

    checkTask.setName("imChannelStatusCheckTask");
    // 主线程关闭时, 子线程也关闭
    checkTask.setDaemon(true);
    checkTask.start();
  }
}
