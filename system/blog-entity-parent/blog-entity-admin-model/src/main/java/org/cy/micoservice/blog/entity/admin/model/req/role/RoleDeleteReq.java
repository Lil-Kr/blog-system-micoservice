package org.cy.micoservice.blog.entity.admin.model.req.role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class RoleDeleteReq implements Serializable {

  @Serial
  private static final long serialVersionUID = 2890801342186527927L;

  @NotNull(message = "surrogateId不能为空")
  private Long surrogateId;
}
