package org.cy.micoservice.blog.message.api.service.impl;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.cy.micoservice.blog.entity.message.model.provider.req.OpenChatReq;
import org.cy.micoservice.blog.framework.rocketmq.starter.producer.RocketMQProducerClient;
import org.cy.micoservice.blog.message.api.config.ApplicationProperties;
import org.cy.micoservice.blog.message.api.service.OpenChatService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;


/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 公共的会话窗口上报service
 */
@Slf4j
@Service
public class OpenChatServiceImpl implements OpenChatService {

  @Resource
  private RocketMQProducerClient producerClient;
  @Resource
  private ApplicationProperties applicationProperties;

  /**
   * 上报当前用户已读消息的offset, 通过MQ实现
   * @param openChatReq
   * @return
   */
  @Override
  public boolean reportInfo(OpenChatReq openChatReq) {
    Message message = new Message();
    message.setTopic(applicationProperties.getOpenChatTopic());
    message.setBody(JSONObject.toJSONString(openChatReq).getBytes(StandardCharsets.UTF_8));
    try {
      SendResult sendResult = producerClient.send(message);
      if(!SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
        log.error("send mq result: {}", sendResult);
        return false;
      }
      return true;
    } catch (Exception e) {
      log.error("send mq fail: ",e);
    }
    return false;
  }
}
