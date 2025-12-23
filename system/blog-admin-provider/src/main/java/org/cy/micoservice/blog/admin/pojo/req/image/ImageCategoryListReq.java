package org.cy.micoservice.blog.admin.pojo.req.image;

import lombok.Data;
import org.cy.micoservice.blog.entity.base.model.BaseReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/3/30
 * @Description:
 */
@Data
public class ImageCategoryListReq extends BaseReq {

  @Serial
  private static final long serialVersionUID = -6480398915923212257L;

  private String name;

  private String remark;

  private Integer status;
}
