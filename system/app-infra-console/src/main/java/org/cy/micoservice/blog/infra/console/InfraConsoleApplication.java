package org.cy.micoservice.blog.infra.console;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"org.cy.micoservice.blog.infra.console.dao"})
public class InfraConsoleApplication {

  public static void main(String[] args) {
    SpringApplication.run(InfraConsoleApplication.class,args);
  }
}