package org.cy.micoservice.blog.audit.facade.dto.text;

import lombok.Data;

/**
 * @Author: Lil-K
 * @Date: 2025/12/15
 * @Description: 图片审核body
 */
@Data
public class ImageAuditBody {

  /**
   * 文本审核内容的类别
   * @see org.cy.micoservice.blog.audit.facade.enums.TextAuditBodyTypeEnum
   */
  private Integer bodyType;

  //图片base64数据内容
  private String body;
}