package org.cy.micoservice.app.audit.facade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum AuditPlatformEnum {

  AL("alibaba","阿里巴巴云平台内容审核"),
  WY("wangyi","网易平台内容审核"),
    ;

  String code;
  String desc;
}
