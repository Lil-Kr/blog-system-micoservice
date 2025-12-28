package org.cy.micoservice.blog.message.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.cy.micoservice.blog.framework.rocketmq.starter.producer.RocketMQProducerClient;
import org.cy.micoservice.blog.im.facade.contstants.ImMessageConstants;
import org.cy.micoservice.blog.im.facade.dto.connector.ImMessageDTO;
import org.cy.micoservice.blog.im.facade.dto.router.ImSingleMessageDTO;
import org.cy.micoservice.blog.im.facade.dto.router.ImBatchMessageDTO;
import org.cy.micoservice.blog.message.facade.dto.req.im.ImChatReqDTO;
import org.cy.micoservice.blog.message.provider.config.MessageApplicationProperties;
import org.cy.micoservice.blog.message.provider.service.ImPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

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
  public void singlePushRouterMessage(ImChatReqDTO imChatReqDTO) {
    ImMessageDTO imMessageDTO = new ImMessageDTO(ImMessageConstants.BIZ_MSG_CODE, JSONObject.toJSONString(imChatReqDTO));
    ImSingleMessageDTO singleMessageDTO = new ImSingleMessageDTO();
    singleMessageDTO.setImMessageDTO(imMessageDTO);
    singleMessageDTO.setReceiverId(imChatReqDTO.getReceiverId());

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

  /**
   * 批量推送消息到
   * @param imChatReqDTOList
   */
  @Override
  public void batchPushRouterMessage(List<ImChatReqDTO> imChatReqDTOList) {
    List<ImSingleMessageDTO> imSingleMessageDTOList = imChatReqDTOList.parallelStream()
      .map(imChatReq -> {
        ImMessageDTO imMessageDTO = new ImMessageDTO(ImMessageConstants.BIZ_MSG_CODE, JSONObject.toJSONString(imChatReq));
        ImSingleMessageDTO singleMessageDTO = new ImSingleMessageDTO();
        singleMessageDTO.setImMessageDTO(imMessageDTO);
        singleMessageDTO.setReceiverId(imChatReq.getReceiverId());
        return singleMessageDTO;
      })
      .collect(Collectors.toList());

    ImBatchMessageDTO imBatchMessageDTO = new ImBatchMessageDTO();
    imBatchMessageDTO.setImSingleMessageDTOList(imSingleMessageDTOList);

    Message message = new Message();
    message.setBody(JSONObject.toJSONString(imBatchMessageDTO).getBytes(StandardCharsets.UTF_8));
    message.setTopic(messageApplicationProperties.getImRoutePushTopic());
    try {
      log.info("topic: {}, push single msg: {}", messageApplicationProperties.getImRoutePushTopic(), JSON.toJSONString(imBatchMessageDTO));
      SendResult sendResult = rocketMQProducerClient.send(message);
      if (!SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
        // 发送失败
        log.error("send batch push mq msg failed.");
      }
    } catch (Exception e) {
      log.error("batchPushMessage error: ", e);
    }
  }
}
