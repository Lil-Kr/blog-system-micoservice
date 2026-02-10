package org.cy.micoservice.blog.user.api.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.user.api.vo.req.TestReq;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

  @NoAuthCheck
  @PostMapping("/test1")
  public ApiResp<String> test(@RequestBody @Valid TestReq req) {
    System.out.println(JSONObject.toJSONString(req));
    return ApiResp.success("参数校验成功");
  }
}