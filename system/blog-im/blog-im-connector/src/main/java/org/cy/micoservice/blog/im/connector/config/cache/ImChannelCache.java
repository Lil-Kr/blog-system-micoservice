package org.cy.micoservice.blog.im.connector.config.cache;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.im.connector.config.contstants.ImAttributeKeyConstants;
import org.cy.micoservice.blog.im.connector.utils.ChannelHandlerContextUtil;
import org.cy.micoservice.blog.im.connector.utils.ContextAttributeUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description:
 * 1. 缓存 用户 与 channelId 的映射关系
 * 2. 缓存 用户 与 ws 连接关系
 */
@Component
public class ImChannelCache {

  private final Map<Long, ChannelHandlerContext> ctxMap = new ConcurrentHashMap<>();

  public ChannelHandlerContext get(Long userId) {
    return ctxMap.getOrDefault(userId, null);
  }

  public void put(Long userId, ChannelHandlerContext ctx) {
    ctxMap.put(userId, ctx);
  }

  public void remove(Long userId) {
    ctxMap.remove(userId);
  }

  public Map<Long, ChannelHandlerContext> getAllChannel() {
    return ctxMap;
  }

  public boolean isEmpty() {
    return MapUtils.isEmpty(ctxMap);
  }

  public void closeAllConnAndClearCache() {
    for (ChannelHandlerContext ctx : this.getAllChannel().values()) {
      ChannelHandlerContextUtil.close(ctx);
    }
    this.getAllChannel().clear();
  }

  /** =================== waitingIdentifyCtxList =================== **/
  private List<ChannelHandlerContext> waitingIdentifyCtxList = new CopyOnWriteArrayList<>();

  public List<ChannelHandlerContext> getWaitingIdentifyCtxList() {
    return this.waitingIdentifyCtxList;
  }

  public void setWaitingIdentifyCtxList(List<ChannelHandlerContext> waitingIdentifyCtxList) {
    this.waitingIdentifyCtxList = waitingIdentifyCtxList;
  }

  public void addWaitingIdentifyCtxList(ChannelHandlerContext ctx) {
    this.waitingIdentifyCtxList.add(ctx);
  }

  public ChannelHandlerContext removeWaitingIdentifyCtx(String channelId) {
    if (StringUtils.isBlank(channelId)) return null;

    // 使用 indexOf 和 remove 方法替代迭代器 remove 操作
    for (int i = 0; i < this.waitingIdentifyCtxList.size(); i++) {
      ChannelHandlerContext ctx = this.waitingIdentifyCtxList.get(i);
      String currentChannelId = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.CHANNEL_ID, String.class);
      if (channelId.equals(currentChannelId)) {
        return this.waitingIdentifyCtxList.remove(i);
      }
    }
    return null;
  }
}
