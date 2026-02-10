package org.cy.micoservice.blog.user.api.service.impl;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.cy.micoservice.blog.framework.rocketmq.starter.producer.RocketMQProducerClient;
import org.cy.micoservice.blog.user.api.config.ApplicationConfig;
import org.cy.micoservice.blog.user.api.service.UserEnterService;
import org.cy.micoservice.blog.user.facade.provider.req.UserEnterInitReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Lil-K
 * @Date: 2025/12/31
 * @Description:
 */
@Slf4j
@Service
public class UserEnterServiceImpl implements UserEnterService {

  @Autowired
  private ApplicationConfig applicationConfig;
  @Resource
  private RocketMQProducerClient producerClient;

  /**
   * 发送MQ通知, 用户进入程序时触发
   * @param req
   * @return
   */
  @Override
  public boolean enter(UserEnterInitReqDTO req) {
    Message message = new Message();
    message.setTopic(applicationConfig.getUserEnterTopic());
    message.setBody(JSONObject.toJSONString(req).getBytes(StandardCharsets.UTF_8));
    try {
      SendResult sendResult = producerClient.send(message);
      if(! SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
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
