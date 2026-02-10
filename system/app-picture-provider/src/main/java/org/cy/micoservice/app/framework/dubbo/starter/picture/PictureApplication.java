package org.cy.micoservice.app.framework.dubbo.starter.picture;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: Lil-K
 * @Date: 2025/11/19
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class PictureApplication {
  
  public static void main(String[] args) throws InterruptedException {
    SpringApplication springApplication = new SpringApplication(PictureApplication.class);
    springApplication.setWebApplicationType(WebApplicationType.NONE);
    springApplication.run(args);
    CountDownLatch count = new CountDownLatch(1);
    count.await();
  }
}
