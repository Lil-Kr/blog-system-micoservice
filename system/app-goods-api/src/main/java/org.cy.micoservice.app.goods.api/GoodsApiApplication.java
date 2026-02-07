package org.cy.micoservice.app.goods.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description: 商品服务 API 应用
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GoodsApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(GoodsApiApplication.class, args);
  }
}