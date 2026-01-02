package org.cy.micoservice.blog.user.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
@MapperScan(basePackages = {"org.cy.micoservice.blog.user.provider.dao"})
public class UserProviderApplication {

  public static void main(String[] args) throws InterruptedException {
    SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
    springApplication.setWebApplicationType(WebApplicationType.NONE);
    springApplication.run(args);
    CountDownLatch count = new CountDownLatch(1);
    count.await();
  }
}