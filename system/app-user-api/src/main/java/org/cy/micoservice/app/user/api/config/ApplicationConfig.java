package org.cy.micoservice.app.user.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/31
 * @Description:
 */
@Data
@Configuration
public class ApplicationConfig {

  /** ======================== topic config ======================== **/
  /**
   * topic:
   */
  @Value("${blog.cookie.domain:web.blog}")
  private String cookieDomain;

  /**
   * topic: 发送用户进入应用信号, 用于用户的未读消息预加载
   */
  @Value("${im.user.enter.producer-topic:blog-user-enter-topic}")
  private String userEnterTopic;
}
