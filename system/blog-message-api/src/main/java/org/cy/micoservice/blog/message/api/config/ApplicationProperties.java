package org.cy.micoservice.blog.message.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 应用配置
 */
@Data
@Configuration
@RefreshScope
public class ApplicationProperties {

  @Value("${im.secret.key:PIhqklxMNarWuqoNFFGJ5QGgesg==hkl1UBGlqopaKHKq9123h1}")
  private String imSecretKey;

  @Value("${im.server.address:ws://localhost:10883/blog/im/chat}")
  private String imServerAddress;

  @Value("${open.chat.topic:blog-open-chat-topic}")
  private String openChatTopic;
}