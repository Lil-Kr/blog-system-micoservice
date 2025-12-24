package org.cy.micoservice.blog.im.gateway.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BlogImWebSocketRouterFilterTest {
  @Test
  public void test1() {
    String format = String.format("%s%s:%s", "ws://", "666", "abc");
    Assertions.assertEquals("ws://666:abc", format);
  }
}