package org.cy.micoservice.blog.framework.dubbo.starter.auth.utils.dateUtil;

import org.cy.micoservice.blog.common.utils.DateUtil;
import org.junit.jupiter.api.Test;

class DateUtilTest {

  @Test
  public void test1() {
    String now = DateUtil.getNowDateTimeForYMD();
  }

}