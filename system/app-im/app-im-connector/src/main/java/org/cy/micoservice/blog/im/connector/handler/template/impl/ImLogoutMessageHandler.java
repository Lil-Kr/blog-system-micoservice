package org.cy.micoservice.blog.im.connector.handler.template.impl;

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.im.connector.config.ImConnectorProperties;
import org.cy.micoservice.blog.im.connector.config.cache.ImChannelCache;
import org.cy.micoservice.blog.im.connector.config.contstants.ImAttributeKeyConstants;
import org.cy.micoservice.blog.im.connector.handler.template.AbstractImMessageHandlerTemplate;
import org.cy.micoservice.blog.im.connector.service.ImMessageSenderService;
import org.cy.micoservice.blog.im.connector.service.ImPushAsyncService;
import org.cy.micoservice.blog.im.connector.utils.ChannelHandlerContextUtil;
import org.cy.micoservice.blog.im.connector.utils.ContextAttributeUtil;
import org.cy.micoservice.blog.im.facade.dto.connector.ImMessageDTO;
import org.cy.micoservice.blog.im.facade.dto.connector.body.ImLogoutBody;
import org.cy.micoservice.blog.im.facade.enums.ImMessageCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description: im登出消息处理器
 */
@Slf4j
@Component("imLogoutMessageHandler")
public class ImLogoutMessageHandler extends AbstractImMessageHandlerTemplate {

  @Autowired
  private ImMessageSenderService senderService;
  @Autowired
  private ImChannelCache imChannelCache;
  @Autowired
  private ImPushAsyncService imPushAsyncTaskService;
  @Autowired
  private ImConnectorProperties imConnectorProperties;

  @Override
  protected boolean isSupport(ImMessageDTO dto) {
    return dto != null && dto.getCode() == ImMessageCodeEnum.LOGOUT.getCode();
  }

  @Override
  public void doMessageHandler(ChannelHandlerContext ctx, ImMessageDTO dto) {
    boolean isLogin = ChannelHandlerContextUtil.isLogin(ctx);
    String userId = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.USER_ID, String.class);
    if(! isLogin || userId == null) {
      // 非法连接
      ChannelHandlerContextUtil.close(ctx);
      return;
    }

    // 根据ctx绑定的uri获取不同的topic
    String topic = this.getCurrentChannelTopic(ctx);
    if (StringUtils.isBlank(topic)) {
      log.error("logout not match im msg here.");
      return;
    }

    ImLogoutBody imLogoutBody = new ImLogoutBody();
    imLogoutBody.setEventTime(System.currentTimeMillis());
    imLogoutBody.setUserId(Long.parseLong(userId));

    imChannelCache.remove(imLogoutBody.getUserId());
    ImMessageDTO logoutAckDTO = new ImMessageDTO(ImMessageCodeEnum.LOGOUT.getCode(), JSONObject.toJSONString(imLogoutBody));
    boolean respStatus = senderService.safeWrite(ctx, logoutAckDTO);
    if (! respStatus) {
      log.error("logout info safe write fail.");
      return;
    }
    boolean sendLogoutMsgStatus = this.sendLogoutMsgToMQ(ctx, logoutAckDTO, topic);
    if (! sendLogoutMsgStatus) {
      log.error("logout info send mq fail.");
    }
  }

  /**
   * send login message into mq, then send login message to im-router layer
   * @param ctx
   * @param dto
   */
  private boolean sendLogoutMsgToMQ(ChannelHandlerContext ctx, ImMessageDTO dto, String topic) {
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

  /**
   * 根据ctx绑定的uri获取不同的topic
   * @param ctx
   * @return
   */
  private String getCurrentChannelTopic(ChannelHandlerContext ctx) {
    Map<String, String> topicMapping = imConnectorProperties.getImLogoutTopicMapping();
    String uri = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.WS_URI, String.class);
    String topic = topicMapping.getOrDefault(uri, null);
    log.info("getCurrentChannelTopic: uri={}, topic={}", uri, topic);
    return topic;
  }
}
