package org.cy.micoservice.blog.message.api.service.impl;

import com.alibaba.fastjson.JSON;
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

  @Override
  public boolean reportInfo(OpenChatReq openChatReq) {
    Message message = new Message();
    message.setTopic(applicationProperties.getOpenChatTopic());
    message.setBody(JSON.toJSONBytes(openChatReq));
    try {
      SendResult sendResult = producerClient.send(message);
      if(!SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
        log.error("send mq result:{}", sendResult);
        return false;
      }
      return true;
    } catch (Exception e) {
      log.error("send mq fail:",e);
    }
    return false;
  }
}
