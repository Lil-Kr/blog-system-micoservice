package org.cy.micoservice.blog.im.connector.handler;

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.im.connector.config.ImConnectorProperties;
import org.cy.micoservice.blog.im.connector.config.cache.ImChannelCache;
import org.cy.micoservice.blog.im.connector.contstants.ImAttributeKeyConstants;
import org.cy.micoservice.blog.im.connector.contstants.ImHandshakeConstants;
import org.cy.micoservice.blog.im.connector.service.ImMessageSenderService;
import org.cy.micoservice.blog.im.connector.service.ImMonitorService;
import org.cy.micoservice.blog.im.connector.service.ImPushAsyncService;
import org.cy.micoservice.blog.im.connector.utils.ContextAttributeUtil;
import org.cy.micoservice.blog.im.facade.router.connector.contstants.ImMessageConstants;
import org.cy.micoservice.blog.im.facade.router.connector.dto.ImMessageDTO;
import org.cy.micoservice.blog.im.facade.router.connector.dto.body.ImShakeHandBody;
import org.cy.micoservice.blog.im.facade.router.connector.enums.ImChannelStatusEnum;
import org.cy.micoservice.blog.im.facade.router.connector.enums.ImMessageCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

/**
 * @Author: Lil-K
 * @Date: 2025/12/9
 * @Description: 处理 websocket协议 握手处理部分逻辑
 * 入站
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketShakeHandler extends ChannelInboundHandlerAdapter {

  @Autowired
  private ImPushAsyncService imPushAsyncTaskService;
  @Autowired
  private ImConnectorProperties imConnectorProperties;
  @Autowired
  private ImChannelCache imChannelCache;
  @Autowired
  private ImMessageSenderService senderService;
  @Autowired
  private ImMonitorService imMonitorService;

  /**
   * 读取数据处理
   * @param ctx
   * @param msg
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    log.info("WebSocketShakeHandler channelRead: {}", Thread.currentThread().getName());

    boolean release = true;
    try {
      // 判断当前请求是否时完整的http数据包
      if (msg instanceof FullHttpRequest) {
        log.info("Received FullHttpRequest for URI: {}", ((FullHttpRequest) msg).uri());
        // 握手协议处理
        this.handleHttpRequest(ctx, (FullHttpRequest) msg);
        return;
      } else if (msg instanceof CloseWebSocketFrame) {
        log.info("Received CloseWebSocketFrame");
        // 关闭连接处理
        this.closeWebSocket(ctx, (CloseWebSocketFrame) msg);
        return;
      }

      release = false;
      // 无异常才允许后续的链路进行处理
      ctx.fireChannelRead(msg);
    } catch (Exception e) {
      log.error("channelRead error:{}", e.getMessage(), e);
    } finally {
      if (msg != null && release) {
        ReferenceCountUtil.release(msg);
      }
    }
  }

  /**
   * 处理 websocket 协议握手处理
   * @param ctx
   * @param msg
   */
  private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
    String uri = msg.uri();
    log.info("Handling WebSocket handshake for URI: {}", uri);

    String webSocketUrl = ImHandshakeConstants.WS_HOST_LOCAL + imConnectorProperties.getWsPort() + uri;

    WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(webSocketUrl, null, false);

    /**
     * 判断握手包版本是否一致
     */
    WebSocketServerHandshaker serverHandshake = factory.newHandshaker(msg);

    if (Objects.isNull(serverHandshake)) {
      WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
      log.warn("Unsupported WebSocket version");
      return;
    }

    // 初始化握手协议通道
    ChannelFuture channelFuture = serverHandshake.handshake(ctx.channel(), msg);
    // 这个位置是后续做主动推送的核心关键
    channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    channelFuture.addListener(future -> {
      if (! future.isSuccess()) {
        log.error("WebSocket handshake failed for URI: {}", uri, future.cause());
        return;
      }

      log.info("WebSocket handshake completed successfully for URI: {}", uri);
      /**
       * 这里可以根据url后缀来区分业务场景:
       * ws://localhost:10880/blog/im/live  --> 直播场景
       * ws://localhost:10880/blog/im/chat  --> 聊天场景
       */
      ContextAttributeUtil.set(ctx, ImAttributeKeyConstants.WS_URI, uri);
      ContextAttributeUtil.set(ctx, ImAttributeKeyConstants.SERVER_HAND_SHAKE_KEY, serverHandshake);
      ContextAttributeUtil.set(ctx, ImAttributeKeyConstants.IDENTIFY_STATUS, ImChannelStatusEnum.WAITING_FOR_IDENTIFY.getCode());
      ContextAttributeUtil.set(ctx, ImAttributeKeyConstants.CHANNEL_ID, UUID.randomUUID().toString());
      ContextAttributeUtil.set(ctx, ImAttributeKeyConstants.SHAKE_HAND_TIME, System.currentTimeMillis());

      ImShakeHandBody shakeHandBody = ImShakeHandBody.builder().traceId(UUID.randomUUID().toString()).build();
      ImMessageDTO dto = new ImMessageDTO(ImMessageCodeEnum.SHAKE_HAND.getCode(), JSONObject.toJSONString(shakeHandBody));
      boolean sendStatus = senderService.safeWrite(ctx, dto);
      if (sendStatus) {
        imChannelCache.addWaitingIdentifyCtxList(ctx);
      }

      // 统计连接数
      imMonitorService.incrConnection();
      log.info("Sent handshake response to client");
    });
  }

  /**
   * 断开连接处理
   * @param ctx
   * @param msg
   */
  private void closeWebSocket(ChannelHandlerContext ctx, CloseWebSocketFrame msg) {
    log.info("Closing WebSocket connection");
    WebSocketServerHandshaker serverHandshake = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.SERVER_HAND_SHAKE_KEY, WebSocketServerHandshaker.class);
    if (Objects.nonNull(serverHandshake)) {
      serverHandshake.close(ctx.channel(), msg.retain());
    }
    ctx.close();
  }

  /**
   * 握手断开连接处理
   * @param ctx
   * @throws Exception
   */
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
    log.info("WebSocketShakeHandler channelInactive: {}", Thread.currentThread().getName());
    // 断开连接时统计断开连接数
    imMonitorService.decrConnection();

    Long userId = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.USER_ID, Long.class);
    if (Objects.isNull(userId)) {
      return;
    }
    imChannelCache.remove(userId);

    String uri = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.WS_URI, String.class);
    if (StringUtils.isBlank(uri) || ! imConnectorProperties.getImLogoutTopicMapping().containsKey(uri)) return;

    String logoutTopic = imConnectorProperties.getImLogoutTopicMapping().get(uri);
    ImMessageDTO dto = new ImMessageDTO(ImMessageConstants.LOGOUT_MSG_CODE, "logout");
    imPushAsyncTaskService.sendAsyncLogoutMessageMQ(JSONObject.toJSONString(dto), logoutTopic);
  }

  /**
   * 握手连接处理
   * @param ctx
   * @throws Exception
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    log.info("WebSocketShakeHandler channelActive: {}", Thread.currentThread().getName());
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    log.info("WebSocketShakeHandler userEventTriggered: {}", evt.getClass().getSimpleName());
    ctx.fireUserEventTriggered(evt);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("WebSocketShakeHandler exception: ", cause);
    ctx.close();
  }
}