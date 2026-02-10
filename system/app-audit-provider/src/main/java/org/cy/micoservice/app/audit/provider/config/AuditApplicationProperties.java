package org.cy.micoservice.app.audit.provider.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 审核服务 properties 配置
 */
@Data
@Configuration
public class AuditApplicationProperties {

  /** ========================= topic ========================= **/
  /**
   * 审核请求 topic
   */
  @Value("${im.common.audit.topic:}")
  private String commonAuditTopic;

  /**
   * 审核结果响应topic
   */
  @Value("${im.common.audit.result.topic:}")
  private String commonAuditResultTopic;

  @Value("${im.chat.message.audit.consumer-topic:}")
  private String imChatMessageAuditTopic;

  @Value("${im.chat.message.audit.result.producer-topic:}")
  private String imChatMessageAuditResultTopic;

  @Value("${im.chat.message.audit.topic.push.size:500}")
  private Integer imChatMessageAuditTopicPushSize;

  @Value("${al.text.audit.weight:10}")
  private Integer alTextAuditWeight;

  @Value("${al.text.audit.weight:10}")
  private Integer wyTextAuditWeight;

  @Value("${al.image.audit.weight:10}")
  private Integer alImageAuditWeight;

  @Value("${al.image.audit.weight:10}")
  private Integer wyImageAuditWeight;

  @Value("${max.text.audit.retry.times:3}")
  private Integer maxTextAuditRetryTimes;

  @Value("${max.image.audit.retry.times:3}")
  private Integer maxImageAuditRetryTimes;
}