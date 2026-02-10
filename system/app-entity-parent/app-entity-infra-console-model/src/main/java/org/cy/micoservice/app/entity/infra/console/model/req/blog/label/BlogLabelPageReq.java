package org.cy.micoservice.app.entity.infra.console.model.req.blog.label;

import lombok.Data;
import org.cy.micoservice.app.entity.base.model.api.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2024/4/4
 * @Description:
 */
@Data
public class BlogLabelPageReq extends BasePageReq {

  @Serial
  private static final long serialVersionUID = 6858743242669678910L;

  private Long surrogateId;

  private Integer number;

  private String name;

  private String remark;
}
