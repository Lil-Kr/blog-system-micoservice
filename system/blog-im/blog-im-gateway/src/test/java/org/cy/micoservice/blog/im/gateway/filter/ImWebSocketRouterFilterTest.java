package org.cy.micoservice.blog.im.gateway.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.cy.micoservice.blog.im.facade.contstants.ImMonitorCacheConstant.IM_CONNECTOR_ADDRESS_KEY;

public class ImWebSocketRouterFilterTest {
  @Test
  public void test1() {
    String format = String.format(IM_CONNECTOR_ADDRESS_KEY, "ws://", "666", "abc");
    Assertions.assertEquals("ws://666:abc", format);
  }
}