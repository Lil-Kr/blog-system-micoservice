package org.cy.micoservice.blog.user.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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

  public static void main(String[] args) {
    SpringApplication.run(UserProviderApplication.class, args);
  }
}