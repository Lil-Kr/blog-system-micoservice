package org.cy.micoservice.blog.entity.admin.model.req.org;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class OrgListAllReq extends BasePageReq {

  @Serial
  private static final long serialVersionUID = 497300992021309354L;

  private Long surrogateId;

  // org number
  private String number;

  // org name
  private String name;

  private Integer seq;

  private String remark;
}
