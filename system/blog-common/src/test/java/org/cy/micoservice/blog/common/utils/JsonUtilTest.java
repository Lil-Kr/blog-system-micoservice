package org.cy.micoservice.blog.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cy.micoservice.blog.common.utils.entity.ImMessageDTO;
import org.junit.Test;

import java.util.Map;

public class JsonUtilTest {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class TokenBodyResponse {
    private String subject;

    private Map<String, Object> authAttrs;
  }

  @Test
  public void test1() {
    Map<String, Object> authAttrs = Map.of("name", "cy", "age", 18);
    TokenBodyResponse tokenBodyResponse = new TokenBodyResponse();
    tokenBodyResponse.setSubject("sss");
    tokenBodyResponse.setAuthAttrs(authAttrs);
    String jsonString = JsonUtil.toJsonString(tokenBodyResponse);
    System.out.println(jsonString);
  }

  @Test
  public void test2() {
    ImMessageDTO dto = new ImMessageDTO(1001, "shake hand success".getBytes());
    String res = JsonUtil.toJsonString(dto);
    System.out.println(res);
  }

}