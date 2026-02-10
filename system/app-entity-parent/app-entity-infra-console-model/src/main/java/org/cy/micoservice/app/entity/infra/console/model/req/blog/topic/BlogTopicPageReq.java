package org.cy.micoservice.app.entity.infra.console.model.req.blog.topic;

import lombok.Data;
import org.cy.micoservice.app.entity.base.model.api.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2024/5/25
 * @Description:
 */
@Data
public class BlogTopicPageReq extends BasePageReq {

  @Serial
  private static final long serialVersionUID = 1922840476308891810L;

  private Long surrogateId;

  private Integer number;

  private String name;

  private String remark;
}
