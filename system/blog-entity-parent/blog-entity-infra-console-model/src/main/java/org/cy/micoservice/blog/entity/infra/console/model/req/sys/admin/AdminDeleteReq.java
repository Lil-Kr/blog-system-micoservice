package org.cy.micoservice.blog.entity.infra.console.model.req.sys.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AdminDeleteReq implements Serializable {

  @Serial
  private static final long serialVersionUID = 7307084519429886380L;

  @NotNull(message = "adminId 不能为空")
  private Long adminId;
}
