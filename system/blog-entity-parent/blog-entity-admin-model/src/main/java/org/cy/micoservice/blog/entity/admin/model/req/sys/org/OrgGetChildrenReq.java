package org.cy.micoservice.blog.entity.admin.model.req.sys.org;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class OrgGetChildrenReq {

  @NotNull(message = "组织surrogateId不能为空")
  private Long surrogateId;

}
