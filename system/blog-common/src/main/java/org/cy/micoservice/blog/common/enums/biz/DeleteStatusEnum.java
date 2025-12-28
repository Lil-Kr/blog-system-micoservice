package org.cy.micoservice.blog.common.enums.biz;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cy.micoservice.blog.common.constants.DeleteStatusConstants;

@Getter
@AllArgsConstructor
public enum DeleteStatusEnum {

  ACTIVE(DeleteStatusConstants.ACTIVE, "活跃"),
  DELETED(DeleteStatusConstants.DELETED, "已删除"),
  ;

  int code;
  String desc;
}
