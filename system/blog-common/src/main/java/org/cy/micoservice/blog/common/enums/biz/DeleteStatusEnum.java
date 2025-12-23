package org.cy.micoservice.blog.common.enums.biz;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeleteStatusEnum {

  ACTIVE(0, "活跃"),
  DELETED(1, "已删除"),
  ;

  Integer code;
  String desc;
}
