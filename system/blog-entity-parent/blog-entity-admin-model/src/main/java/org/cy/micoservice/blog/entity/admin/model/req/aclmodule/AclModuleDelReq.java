package org.cy.micoservice.blog.entity.admin.model.req.aclmodule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class AclModuleDelReq implements Serializable {

  @Serial
  private static final long serialVersionUID = 160598866866516774L;

  private Long id;

  /**
   * 权限模块surrogateId,唯一主键
   */
  @NotNull(message = "权限模块surrogateId不能为空")
  private Long surrogateId;
}
