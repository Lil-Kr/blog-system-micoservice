package org.cy.micoservice.blog.message.provider.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 消息服务 properties 配置
 */
@Data
@Configuration
public class MessageApplicationProperties {

  /**
   * topic: 消费从 im-connector 发送来的聊天消息
   */
  @Value("${im.chat.message.biz.consumer-topic:}")
  private String imBizMessageConsumerTopic;

  @Value("${im.chat.message.biz.consumer.batch.size:200}")
  private Integer imBizMessageConsumerBatchSize;

  /**
   * topic: 发送消息到 审核平台
   */
  @Value("${im.chat.message.audit.producer-topic:}")
  private String imChatMessageAuditTopic;

  /**
   * topic: 消费 audit 审核后发送过来的消息
   */
  @Value("${im.chat.message.audit.result.consumer-topic:}")
  private String imChatMessageAuditResultTopic;

  /**
   * topic: 推送消息给 im-router IM路由服务
   */
  @Value("${im.route.push.producer-topic:}")
  private String imRoutePushTopic;

}