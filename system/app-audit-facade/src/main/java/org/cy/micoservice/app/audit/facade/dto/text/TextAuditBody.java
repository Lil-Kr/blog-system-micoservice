package org.cy.micoservice.app.audit.facade.dto.text;

import lombok.Data;
import org.cy.micoservice.app.audit.facade.enums.TextAuditBodyTypeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 文本内容解析
 */
@Data
public class TextAuditBody implements Serializable {

  @Serial
  private static final long serialVersionUID = 5281395949064142722L;

  /**
   * 文本审核内容的类别
   * @see TextAuditBodyTypeEnum
   */
  private Integer bodyType;

  //文本审核的参数体
  private String body;
}