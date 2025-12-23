package org.cy.micoservice.blog.admin.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Lil-K
 * @Date: 2025/12/13
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AdminApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(AdminApiApplication.class, args);
  }
}
