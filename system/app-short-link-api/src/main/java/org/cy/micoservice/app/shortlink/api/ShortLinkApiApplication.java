package org.cy.micoservice.app.shortlink.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: Lil-K
 * @Date: 2026/2/15
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableAsync
public class ShortLinkApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShortLinkApiApplication.class, args);
  }
}