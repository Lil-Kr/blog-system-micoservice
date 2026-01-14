package org.cy.micoservice.blog.test.controller;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.framework.web.starter.web.RequestContext;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/11/21
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

  // @Autowired
  // private InfraConsoleClient infraConsoleClient;

  @NoAuthCheck
  @GetMapping("/test1")
  public String test1(HttpServletRequest request) {
    log.info("GET success:{}", "test1");
    return "test-app test1 success";
  }

  @NoAuthCheck
  @PostMapping("/test2")
  public String test1(@RequestBody Map<Object, Object> req) {
    log.info("POST success:{}", JSONObject.toJSONString(req));
    return "test-app test2 success";
  }

  @GetMapping("/test3")
  public String test3() {
    log.info("GET success:{}", "test3");
    Long userId = RequestContext.getUserId();
    return "test-app test3 success" + "userId: " + userId;
  }

  // @NoAuthCheck
  // @GetMapping("/create")
  // public ApiResp<Long> create() {
  //   RouteConfigSaveRequest req = new RouteConfigSaveRequest();
  //   req.setSchema("http");
  //   req.setMethod("GET");
  //   req.setPath("/api/test/test1");
  //   req.setUri("lb://test-app");
  //   req.setAuthType("jwt");
  //   return infraConsoleClient.createRouteConfig(req);
  // }
}