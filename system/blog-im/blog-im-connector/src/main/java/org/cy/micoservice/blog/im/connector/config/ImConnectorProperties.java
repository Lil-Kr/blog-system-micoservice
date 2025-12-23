package org.cy.micoservice.blog.im.connector.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/12/9
 * @Description:
 */
@Data
@Configuration
public class ImConnectorProperties {

  /**
   * websocket启动的端口
   */
  @Value("${im.ws.port:10880}")
  private Integer wsPort;

  /**
   * im消息针对不同uri场景下的传递topic配置
   */
  @Value("#{${im.login.topic.mapping:}}")
  private Map<String, String> imLoginTopicMapping;

  @Value("#{${im.logout.topic.mapping:}}")
  private Map<String, String> imLogoutTopicMapping;

  @Value("#{${im.biz.topic.mapping:}}")
  private Map<String, String> imUriTopicMapping;

  @Value("${im.max.channel.retry.queue.size:10000}")
  private Integer maxChannelRetryQueueSize;

  @Value("${im.max.shake.hand.timeout:10}")
  private Integer maxShakeHandTimeOut;

  @Value("${spring.cloud.nacos.discovery.server-addr:}")
  private String nacosAddress;

  @Value("${spring.cloud.nacos.username:nacos}")
  private String nacosUsername;

  @Value("${spring.cloud.nacos.password:nacos}")
  private String nacosPassword;

  @Value("${spring.cloud.nacos.discovery.namespace:}")
  private String nacosNamespace;

  @Value("${im.connector.cluster.name:blog-im-connector-cluster}")
  private String imConnectorClusterName;

  @Value("${dubbo.protocol.port}")
  private Integer dubboProtocolPort;
}
