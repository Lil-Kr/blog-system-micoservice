package org.cy.micoservice.blog.im.connector.handler.template.impl;

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.im.connector.config.ImConnectorProperties;
import org.cy.micoservice.blog.im.connector.handler.template.AbstractImMessageHandlerTemplate;
import org.cy.micoservice.blog.im.connector.service.ImMessageSenderService;
import org.cy.micoservice.blog.im.connector.service.ImPushAsyncService;
import org.cy.micoservice.blog.im.connector.utils.ChannelHandlerContextUtil;
import org.cy.micoservice.blog.im.connector.utils.ContextAttributeUtil;
import org.cy.micoservice.blog.im.connector.config.contstants.ImAttributeKeyConstants;
import org.cy.micoservice.blog.im.facade.dto.connector.ImMessageDTO;
import org.cy.micoservice.blog.im.facade.dto.connector.body.ImBizMsgResp;
import org.cy.micoservice.blog.im.facade.enums.ImMessageCodeEnum;
import org.cy.micoservice.blog.im.facade.enums.ImMessageStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description: im中台获取业务消息之后 的处理器
 */
@Slf4j
@Component("imBizMessageHandler")
public class ImBizMessageHandler extends AbstractImMessageHandlerTemplate {

  @Autowired
  private ImConnectorProperties imConnectorProperties;
  @Autowired
  private ImPushAsyncService imPushAsyncTaskService;
  @Autowired
  private ImMessageSenderService senderServices;

  @Override
  protected boolean isSupport(ImMessageDTO dto) {
    return dto != null && dto.getCode() == ImMessageCodeEnum.BIZ.getCode();
  }

  @Override
  public void doMessageHandler(ChannelHandlerContext ctx, ImMessageDTO dto) {
    boolean isLogin = ChannelHandlerContextUtil.isLogin(ctx);
    String userId = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.USER_ID, String.class);
    if (! isLogin || StringUtils.isBlank(userId)) {
      // 非法ws连接
      ChannelHandlerContextUtil.close(ctx);
      return;
    }

    // 获取topic
    String topic = this.getCurrentChannelTopic(ctx);
    if (StringUtils.isBlank(topic)) {
      log.error("not match im msg here.");
      return;
    }

    /**
     * 传递给到业务下游做处理
     * 下游业务有很多种
     * 使用 rocketmq 进行服务的下游通信
     */
    this.sendBizMsgToMQ(ctx, dto, topic, userId);
    log.info("ImBizMessageHandler sent message to topic: {}, userId: {}", topic, userId);
  }

  /**
   * 根据ctx绑定的uri获取不同的topic
   * @param ctx
   * @return
   */
  private String getCurrentChannelTopic(ChannelHandlerContext ctx) {
    Map<String, String> topicMapping = imConnectorProperties.getImUriTopicMapping();
    String uri = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.WS_URI, String.class);
    String topic = topicMapping.getOrDefault(uri, null);
    log.info("getCurrentChannelTopic: uri={}, topic={}", uri, topic);
    return topic;
  }

  /**
   * send biz message to mq
   * @param ctx
   * @param dto
   */
  private void sendBizMsgToMQ(ChannelHandlerContext ctx, ImMessageDTO dto, String topic, String userId) {
    try {
      String traceId = UUID.randomUUID().toString();
      dto.setTraceId(traceId);
      dto.setSenderId(Long.parseLong(userId));
      log.info("send to message provider, send body: {}", dto);
      // 发送 mq
      boolean sendStatus = imPushAsyncTaskService.sendAsyncBizMessageMQ(JSONObject.toJSONString(dto), topic);
      if (sendStatus) {
        ImBizMsgResp bizMsgResp = ImBizMsgResp.builder()
          .status(ImMessageStatusEnum.SEND_BIZ_MSG_SUCCESS.getCode())
          .traceId(traceId)
          .build();
        ImMessageDTO sendBizRespMsg = new ImMessageDTO(ImMessageCodeEnum.BIZ.getCode(), JSONObject.toJSONString(bizMsgResp));
        senderServices.safeWrite(ctx, sendBizRespMsg);
      }
    } catch (Exception e) {
      log.error("send mq msg error:", e);
    }
  }
}