package com.cy.user;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: Lil-K
 * @Date: 2022/12/10
 * @Description:
 */
@Slf4j
@ServletComponentScan
@EnableAsync // 开启异步注解功能
@EnableScheduling // 开启基于注解的定时任务
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.cy.user.dao")
@SpringBootApplication
public class UserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);


    }
}
