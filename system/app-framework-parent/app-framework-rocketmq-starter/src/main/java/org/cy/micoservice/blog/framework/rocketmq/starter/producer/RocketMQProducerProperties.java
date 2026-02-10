package org.cy.micoservice.blog.framework.rocketmq.starter.producer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Lil-K
 * @Date: Created in 2025/8/3
 * @Description: rocketmq相关配置
 */
@Data
@ConfigurationProperties(prefix = "blog.rmq.producer")
public class RocketMQProducerProperties {

  private String nameserver;

  private String group;

  private Integer sendMsgTimeout;

  private Integer retryTimes;

}
