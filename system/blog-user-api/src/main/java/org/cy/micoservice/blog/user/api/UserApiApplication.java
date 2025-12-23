package org.cy.micoservice.blog.user.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserApiApplication.class, args);
  }
}