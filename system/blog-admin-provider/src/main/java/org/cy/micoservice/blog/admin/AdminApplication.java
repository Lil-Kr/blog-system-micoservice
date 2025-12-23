package org.cy.micoservice.blog.admin;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
@MapperScan(basePackages = {"org.cy.micoservice.blog.admin.dao"})
@EnableMongoRepositories(basePackages = "org.cy.micoservice.blog.admin.dao")
public class AdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(AdminApplication.class, args);
  }
}
