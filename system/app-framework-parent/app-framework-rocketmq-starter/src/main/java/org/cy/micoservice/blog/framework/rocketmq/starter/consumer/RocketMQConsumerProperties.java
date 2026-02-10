package org.cy.micoservice.blog.framework.rocketmq.starter.consumer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "blog.rmq.consumer")
public class RocketMQConsumerProperties {

  private String nameserver;

  private String group;
}