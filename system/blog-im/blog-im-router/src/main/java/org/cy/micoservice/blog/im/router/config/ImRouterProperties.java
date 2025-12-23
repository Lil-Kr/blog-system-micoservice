package org.cy.micoservice.blog.im.router.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/16
 * @Description:
 */
@Data
@Configuration
public class ImRouterProperties {

  /**
   * topic: 消费 im-connector 发送来的登录消息
   */
  @Value("${im.chat.message.login.consumer-topic:}")
  private String imLoginConsumerTopic;

  /**
   * topic: 消费 im-connector 发送来的登出消息
   */
  @Value("${im.chat.message.logout.consumer-topic:}")
  private String imLogoutConsumerTopic;

  /**
   * topic: 
   */
  @Value("${im.chat.message.biz.consumer-topic:}")
  private String imBizMessageConsumerTopic;

  /**
   * topic:
   */
  @Value("${im.route.topic:}")
  private String imRouteTopic;

  /**
   * topic: 消费 message 服务端推送过来的的消息
   */
  @Value("${im.route.push.consumer-topic:}")
  private String imRoutePushTopic;

  @Value("${im.route.topic.consumer.batch.size:500}")
  private Integer imRouteTopicConsumerBatchSize;

  @Value("${im.chat.message.login.consumer.batch.size:200}")
  private Integer imLoginConsumerBatchSize;

  @Value("${im.chat.message.logout.consumer.batch.size:200}")
  private Integer imLogoutConsumerBatchSize;

  @Value("${im.chat.message.biz.consumer.batch.size:500}")
  private Integer imBizMessageConsumerBatchSize;

}