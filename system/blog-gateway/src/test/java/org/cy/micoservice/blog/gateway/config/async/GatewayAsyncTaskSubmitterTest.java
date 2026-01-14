package org.cy.micoservice.blog.gateway.config.async;

import com.alibaba.fastjson2.JSONArray;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class GatewayAsyncTaskSubmitterTest {

  private GatewayAsyncTaskSubmitter gatewayAsyncTaskSubmitter = new GatewayAsyncTaskSubmitter();

  @Test
  public void test1() throws Exception {
    CompletableFuture<List<String>> relationFuture =
      gatewayAsyncTaskSubmitter.supplyAsync(
        "query-chat-relation",
        () -> {
          try {
            TimeUnit.MILLISECONDS.sleep(100);
            List<String> a = new ArrayList<>();
            a.add("a");
            return a;
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        },
        Collections::emptyList,
        200L
      );
    CompletableFuture.allOf(relationFuture).join();
    List<String> list = relationFuture.get();
    Assertions.assertEquals("[\"a\"]", JSONArray.toJSONString(list));
  }

  @Test
  public void test2() throws Exception {
    CompletableFuture<List<String>> relationFuture =
      gatewayAsyncTaskSubmitter.supplyAsync(
        "query-chat-relation",
        () -> {
          try {
            TimeUnit.MILLISECONDS.sleep(500);
            List<String> a = new ArrayList<>();
            a.add("a");
            return a;
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        },
        Collections::emptyList,
        200L
      );
    CompletableFuture.allOf(relationFuture).join();
    List<String> list = relationFuture.get();
    Assertions.assertEquals("[]", JSONArray.toJSONString(list));
  }
}