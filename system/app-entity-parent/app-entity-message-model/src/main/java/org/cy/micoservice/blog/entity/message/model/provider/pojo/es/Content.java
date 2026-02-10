package org.cy.micoservice.blog.entity.message.model.provider.pojo.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Content implements Serializable {

  @Serial
  private static final long serialVersionUID = -3124149548368964811L;

  /**
   * 对话类型: org.cy.micoservice.blog.message.facade.enums.ChatRelationTypeEnum
   */
  private Integer type;

  /**
   * 对话内容
   */
  private String body;
}