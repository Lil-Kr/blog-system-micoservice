package org.cy.micoservice.blog.entity.base.model.provider;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: provider base page req dto
 */
@Data
public class BasePageReqDTO implements Serializable {
  @Serial
  private static final long serialVersionUID = 9119261192787695755L;

  private Integer currentPageNum;

  private Integer pageSize;
}
