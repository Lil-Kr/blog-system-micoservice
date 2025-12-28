package org.cy.micoservice.blog.admin.pojo.req.blog.category;

import lombok.Data;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/3/28
 * @Description:
 */
@Data
public class BlogCategoryPageReq extends BasePageReq {

  @Serial
  private static final long serialVersionUID = 7850723733268763469L;

  private Long surrogateId;

  private String number;

  private String name;

  private String color;

  private String remark;
}
