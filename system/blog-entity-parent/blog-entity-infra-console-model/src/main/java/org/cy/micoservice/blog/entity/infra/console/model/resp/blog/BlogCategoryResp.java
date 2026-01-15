package org.cy.micoservice.blog.entity.infra.console.model.resp.blog;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.infra.console.model.entity.blog.BlogCategory;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
public class BlogCategoryResp extends BlogCategory implements Serializable {

  @Serial
  private static final long serialVersionUID = 7164477679970664133L;
}
