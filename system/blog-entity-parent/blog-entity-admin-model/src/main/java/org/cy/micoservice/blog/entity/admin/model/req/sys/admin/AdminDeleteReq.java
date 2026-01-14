package org.cy.micoservice.blog.entity.admin.model.req.sys.admin;

import jakarta.validation.constraints.NotNull;
import lombok.ToString;

@ToString
public class AdminDeleteReq {

  @NotNull(message = "id不能为空")
  private Long id;

  @NotNull(message = "surrogateId不能为空")
  private Long surrogateId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSurrogateId() {
    return surrogateId;
  }

  public void setSurrogateId(Long surrogateId) {
    this.surrogateId = surrogateId;
  }
}
