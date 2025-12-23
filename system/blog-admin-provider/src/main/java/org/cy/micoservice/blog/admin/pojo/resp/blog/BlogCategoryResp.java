package org.cy.micoservice.blog.admin.pojo.resp.blog;

import lombok.Data;
import lombok.ToString;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogCategory;

import java.io.Serial;
import java.io.Serializable;

@ToString
@Data
public class BlogCategoryResp extends BlogCategory implements Serializable {

  @Serial
  private static final long serialVersionUID = 7164477679970664133L;
}
