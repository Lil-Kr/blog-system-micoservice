package org.cy.micoservice.app.message.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MessageApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(MessageApiApplication.class, args);
  }
}
