package org.cy.micoservice.blog.admin.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
@MapperScan(basePackages = {"org.cy.micoservice.blog.admin.provider.dao"})
@EnableMongoRepositories(basePackages = "org.cy.micoservice.blog.admin.provider.dao")
public class AdminProviderApplication {

  public static void main(String[] args) throws InterruptedException {
    SpringApplication springApplication = new SpringApplication(AdminProviderApplication.class);
    springApplication.setWebApplicationType(WebApplicationType.NONE);
    springApplication.run(args);
    CountDownLatch count = new CountDownLatch(1);
    count.await();
  }
}
