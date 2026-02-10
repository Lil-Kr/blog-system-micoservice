package org.cy.micoservice.blog.entity.infra.console.model.req.blog.label;

import lombok.Data;
import org.cy.micoservice.blog.entity.base.model.api.BaseReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2024/3/31
 * @Description:
 */
@Data
public class BlogLabelListReq extends BaseReq {

  @Serial
  private static final long serialVersionUID = -7963615286211942759L;

  private Long surrogateId;

  private Integer number;

  private String name;

  private String remark;

}
