package org.cy.micoservice.app.shortlink.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Lil-K
 * @Date: 2026/2/15
 * @Description:
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class ShortLinkProviderApplication {

  public static void main(String[] args) throws InterruptedException {
    SpringApplication springApplication = new SpringApplication(ShortLinkProviderApplication.class);
    springApplication.setWebApplicationType(WebApplicationType.NONE);
    springApplication.run(args);
  }
}