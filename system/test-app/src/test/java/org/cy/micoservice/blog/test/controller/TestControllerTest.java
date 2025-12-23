package org.cy.micoservice.blog.test.controller;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class TestControllerTest {

  private RateLimiter limiter;

//  @Before
  public void init() {
    // 每秒生成 3 个令牌
//    limiter = RateLimiter.create(3);
    limiter = RateLimiter.create(3, 60, TimeUnit.SECONDS);
  }

//  @Test
  public void testLimit() {
    while (true) {
      boolean acquire = limiter.tryAcquire();
      if (acquire) {
        doBusiness();
      }
    }
  }

  public void doBusiness() {
    System.out.println("do business...");
  }
}