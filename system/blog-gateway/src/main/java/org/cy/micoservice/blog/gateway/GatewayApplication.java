package org.cy.micoservice.blog.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Lil-K
 * @Date: 2025/11/21
 * @Description: Gateway
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.cy.micoservice.blog.gateway.dao"})
public class GatewayApplication {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(GatewayApplication.class);
    springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
    springApplication.run(args);
  }
}