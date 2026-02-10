package org.cy.micoservice.blog.entity.infra.console.model.req.sys.aclmodule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AclModuleGetReq implements Serializable {

  @Serial
  private static final long serialVersionUID = 160598866866516774L;

  private Long id;

  /**
   * 权限模块surrogateId,唯一主键
   */
  @NotNull(message = "权限模块surrogateId不能为空")
  private Long surrogateId;
}
