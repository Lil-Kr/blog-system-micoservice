package org.cy.micoservice.blog.entity.admin.model.req.sys.org;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrgDeleteReq {

  /**
   * 自增主键
   */
  @NotNull(message = "自增id不能为空")
  private Long id;

  /**
   * 组织唯一主键
   */
  @NotNull(message = "组织surrogateId不能为空")
  private Long surrogateId;
}
