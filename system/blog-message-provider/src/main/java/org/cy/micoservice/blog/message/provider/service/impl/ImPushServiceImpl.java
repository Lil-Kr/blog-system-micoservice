package org.cy.micoservice.blog.message.provider.service.impl;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.cy.micoservice.blog.framework.rocketmq.starter.producer.RocketMQProducerClient;
import org.cy.micoservice.blog.im.facade.router.connector.contstants.ImMessageConstants;
import org.cy.micoservice.blog.im.facade.router.connector.dto.ImMessageDTO;
import org.cy.micoservice.blog.im.facade.router.connector.dto.ImSingleMessageDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReq;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.cy.micoservice.blog.message.provider.service.ImPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 推送消息给router层 service层实现类
 */
@Slf4j
@Service
public class ImPushServiceImpl implements ImPushService {

  @Autowired
  private MessageApplicationProperties messageApplicationProperties;
  @Autowired
  private RocketMQProducerClient rocketMQProducerClient;

  @Override
  public void pushRouterSingleMessage(ImChatReq imChatReq) {
    ImMessageDTO imMessageDTO = new ImMessageDTO(ImMessageConstants.BIZ_MSG_CODE, JSONObject.toJSONString(imChatReq));
    ImSingleMessageDTO singleMessageDTO = new ImSingleMessageDTO();
    singleMessageDTO.setImMessageDTO(imMessageDTO);
    singleMessageDTO.setReceiverId(imChatReq.getReceiverId());

    Message message = new Message();
    message.setBody(JSONObject.toJSONString(singleMessageDTO).getBytes(StandardCharsets.UTF_8));
    message.setTopic(messageApplicationProperties.getImRoutePushTopic());
    try {
      SendResult sendResult = rocketMQProducerClient.send(message);
      if (!SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
        //发送失败
        log.error("send mq msg failed");
      }
    } catch (Exception e) {
      log.error("ImPushServiceImpl error: {}", e.getMessage());
    }
  }
}
