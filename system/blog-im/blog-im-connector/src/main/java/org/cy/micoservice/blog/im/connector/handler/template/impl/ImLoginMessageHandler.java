package org.cy.micoservice.blog.im.connector.handler.template.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.NetUtils;
import org.cy.micoservice.blog.framework.identiy.starter.config.AuthProperties;
import org.cy.micoservice.blog.framework.identiy.starter.uitls.JWTUtil;
import org.cy.micoservice.blog.im.connector.config.ImConnectorProperties;
import org.cy.micoservice.blog.im.connector.config.cache.ImChannelCache;
import org.cy.micoservice.blog.im.connector.config.contstants.ImAttributeKeyConstants;
import org.cy.micoservice.blog.im.connector.handler.template.AbstractImMessageHandlerTemplate;
import org.cy.micoservice.blog.im.connector.service.ImMessageSenderService;
import org.cy.micoservice.blog.im.connector.service.ImPushAsyncService;
import org.cy.micoservice.blog.im.connector.utils.ChannelHandlerContextUtil;
import org.cy.micoservice.blog.im.connector.utils.ContextAttributeUtil;
import org.cy.micoservice.blog.im.facade.connector.dto.ImMessageDTO;
import org.cy.micoservice.blog.im.facade.connector.dto.body.ImLoginBody;
import org.cy.micoservice.blog.im.facade.connector.enums.ImChannelStatusEnum;
import org.cy.micoservice.blog.im.facade.connector.enums.ImMessageCodeEnum;
import org.cy.micoservice.blog.im.facade.connector.enums.ImMessageStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * @Author: Lil-K
 * @Date: 2025/12/10
 * @Description: IM 系统登录消息处理器
 */
@Slf4j
@Component("imLoginMessageHandler")
public class ImLoginMessageHandler extends AbstractImMessageHandlerTemplate {

  @Autowired
  private ImConnectorProperties imConnectorProperties;
  @Autowired
  private AuthProperties authProperties;
  @Autowired
  private ImPushAsyncService imPushAsyncTaskService;
  @Autowired
  private ImChannelCache imChannelCache;
  @Autowired
  private ImMessageSenderService senderService;

  @Override
  protected boolean isSupport(ImMessageDTO dto) {
    return dto != null && dto.getCode() == ImMessageCodeEnum.LOGIN.getCode();
  }

  @Override
  public void doMessageHandler(ChannelHandlerContext ctx, ImMessageDTO dto) {
    ImLoginBody imLoginBody = JSON.parseObject(dto.getBody(), ImLoginBody.class);
    String token = imLoginBody.getToken();
    boolean isTokenValid = this.validateToken(token);
    // token 合法, 可以进行登录处理
    if (! isTokenValid) {
      log.error("jwt error: {}", dto.getBody());
      // 强制关闭连接
      ChannelHandlerContextUtil.close(ctx);
      return;
    }

    // 根据ctx绑定的uri获取不同的topic
    String topic = this.getCurrentChannelTopic(ctx);
    if (StringUtils.isBlank(topic)) {
      log.error("login not match im msg here.");
      return;
    }

    // 获取jwt中的userId信息
    String userId = JWTUtil.extractSubject(token, authProperties.getSecretKey());
    ContextAttributeUtil.set(ctx, ImAttributeKeyConstants.USER_ID, userId);

    ImLoginBody loginBody = new ImLoginBody();
    loginBody.setStatus(ImMessageStatusEnum.IDENTIFY_SUCCESS.getCode());
    loginBody.setUserId(Long.parseLong(userId));
    loginBody.setImConnectorAddress(this.readLocalAddress());
    ImMessageDTO loginSuccessMsg = new ImMessageDTO(ImMessageCodeEnum.LOGIN.getCode(), JSONObject.toJSONString(loginBody));
    boolean respStatus = senderService.safeWrite(ctx, loginSuccessMsg);
    if (! respStatus) {
      log.error("login info safe write fail");
      return;
    }

    // feed back client login success message, then send to MQ
    boolean sendLoginMsgStatus = this.sendLoginMsgToMQ(ctx, loginSuccessMsg, topic);
    if (! sendLoginMsgStatus) return;

    // 最后设置连接成功, 最大可能避免出现非法连接占用的问题 或者 返回响应给客户端出现异常
    ContextAttributeUtil.set(ctx, ImAttributeKeyConstants.IDENTIFY_STATUS, ImChannelStatusEnum.HAS_IDENTIFY.getCode());

    String channelId = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.CHANNEL_ID, String.class);
    imChannelCache.removeWaitingIdentifyCtx(channelId);
    imChannelCache.put(Long.parseLong(userId), ctx);
  }

  /**
   * 获取 Dubbo 端口 (从环境变量或配置)
   * @return
   */
  private String readLocalAddress() {
    return String.format("%s:%s", NetUtils.getLocalHost(), imConnectorProperties.getDubboProtocolPort());
  }

  /**
   * 校验token内容
   * @param jwt
   * @return
   */
  private boolean validateToken(String jwt) {
    return JWTUtil.validateToken(jwt, authProperties.getSecretKey());
  }

  /**
   * 根据ctx绑定的uri获取不同的topic
   * @param ctx
   * @return
   */
  private String getCurrentChannelTopic(ChannelHandlerContext ctx) {
    Map<String, String> topicMapping = imConnectorProperties.getImLoginTopicMapping();
    String uri = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.WS_URI, String.class);
    String topic = topicMapping.getOrDefault(uri, null);
    log.info("getCurrentChannelTopic: uri={}, topic={}", uri, topic);
    return topic;
  }

  /**
   * send login message into mq, then send login message to im-router layer
   * @param ctx
   * @param dto
   */
  private boolean sendLoginMsgToMQ(ChannelHandlerContext ctx, ImMessageDTO dto, String topic) {
    try {
      String traceId = UUID.randomUUID().toString();
      dto.setTraceId(traceId);
      // 发送mq
      return imPushAsyncTaskService.sendAsyncLoginMessageMQ(JSONObject.toJSONString(dto), topic);
    } catch (Exception e) {
      log.error("send mq msg error:", e);
    }
    return false;
  }
}
