package org.cy.micoservice.blog.admin.api.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Lil-K
 * @Date: 2025/3/31
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping("/healthcheck")
  public String healthcheck() {
    return "on";
  }
}