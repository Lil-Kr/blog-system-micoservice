package org.cy.micoservice.blog.im.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description: websocket服务的网关转发程序
 */
@SpringBootApplication
public class WebSocketGatewayApplication {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(WebSocketGatewayApplication.class);
    /**
     * 底层是采用了webflux的响应式编程
     */
    springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
    springApplication.run(args);
  }
}