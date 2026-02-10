package org.cy.micoservice.blog.im.connector.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.cy.micoservice.blog.framework.rocketmq.starter.producer.RocketMQProducerClient;
import org.cy.micoservice.blog.im.connector.service.ImPushAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description:
 */
@Slf4j
@Service
public class ImPushAsyncServiceImpl implements ImPushAsyncService {

  @Autowired
  private RocketMQProducerClient rocketMQProducerClient;

  @Override
  public boolean sendAsyncBizMessageMQ(byte[] body, String dispatchTopic) throws Exception {
    return this.sendAsyncMessageToMq(body, dispatchTopic);
  }

  @Override
  public boolean sendAsyncLoginMessageMQ(byte[] body, String imLoginTopic) throws Exception {
    return this.sendAsyncMessageToMq(body, imLoginTopic);
  }

  @Override
  public boolean sendAsyncLogoutMessageMQ(byte[] body, String imLogoutTopic) throws Exception {
    return this.sendAsyncMessageToMq(body, imLogoutTopic);
  }

  // =================== async mq ===================

  @Override
  public boolean sendAsyncBizMessageMQ(String msg, String topic) throws Exception {
    return this.sendAsyncMessageToMq(msg, topic);
  }

  @Override
  public boolean sendAsyncLoginMessageMQ(String msg, String imLoginTopic) throws Exception {
    return this.sendAsyncMessageToMq(msg, imLoginTopic);
  }

  @Override
  public boolean sendAsyncLogoutMessageMQ(String msg, String imLogoutTopic) throws Exception {
    return this.sendAsyncMessageToMq(msg, imLogoutTopic);
  }

  /**
   * send mq info
   * @param body
   * @param topic
   */
  private boolean sendAsyncMessageToMq(byte[] body, String topic) throws Exception {
    Message message = new Message();
    message.setBody(body);
    message.setTopic(topic);
    SendResult sendResult = null;
    try {
      sendResult = rocketMQProducerClient.send(message);
    } catch (Exception e) {
      log.error("im message send failed!", e);
      throw e;
    }
    /**
     * 如果业务量大可以拆分多个 topic
     * 如果遇到堆积情况, 可以起到保护隔离作用, 减小爆炸半径
     */
    if (! SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
      log.error("im message send failed!");
      return false;
    }
    return true;
  }

  /**
   *
   * @param msg
   * @param topic
   */
  private boolean sendAsyncMessageToMq(String msg, String topic) throws Exception {
    Message message = new Message();
    message.setBody(msg.getBytes(StandardCharsets.UTF_8));
    /**
     * 如果业务量大可以拆分多个 topic
     * 如果遇到堆积情况, 可以起到保护隔离作用, 减小爆炸半径
     */
    message.setTopic(topic);
    SendResult sendResult = null;
    try {
      sendResult = rocketMQProducerClient.send(message);
    } catch (Exception e) {
      log.error("im message send failed!", e);
      throw e;
    }
    if (! SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
      log.error("im message send failed!");
      return false;
    }
    return true;
  }
}
