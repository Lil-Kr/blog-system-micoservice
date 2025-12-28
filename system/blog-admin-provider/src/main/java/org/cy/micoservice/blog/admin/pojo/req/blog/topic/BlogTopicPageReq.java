package org.cy.micoservice.blog.admin.pojo.req.blog.topic;

import lombok.Data;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;

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
