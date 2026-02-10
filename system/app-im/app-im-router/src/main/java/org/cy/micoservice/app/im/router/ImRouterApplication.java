package org.cy.micoservice.app.im.router;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Lil-K
 * @Date: 2025/12/9
 * @Description: im 路由层启动程序
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class ImRouterApplication {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(ImRouterApplication.class);
    springApplication.setWebApplicationType(WebApplicationType.NONE);
    springApplication.run(args);
  }
}