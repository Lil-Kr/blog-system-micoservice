package org.cy.micoservice.blog.framework.dubbo.starter.picture;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Lil-K
 * @Date: 2025/11/19
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class PictureApplication {
  public static void main(String[] args) {
    SpringApplication.run(PictureApplication.class, args);
  }
}
