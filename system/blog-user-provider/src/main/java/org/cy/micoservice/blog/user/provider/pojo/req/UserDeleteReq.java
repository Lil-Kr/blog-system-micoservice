package org.cy.micoservice.blog.user.provider.pojo.req;

import lombok.Data;
import lombok.ToString;

//import jakarta.validation.constraints.NotNull;

@ToString
@Data
public class UserDeleteReq {

//  @NotNull(message = "id不能为空")
  private Long id;

//  @NotNull(message = "surrogateId不能为空")
  private Long surrogateId;
}
