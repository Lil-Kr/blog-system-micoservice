package org.cy.micoservice.app.entity.user.model.provider.req;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserUpdatePwdReq {

//  @NotNull(message = "surrogateId不能为空")
  private Long surrogateId;

//  @NotNull(message = "用户名不能为空")
//  @Size(max = 50, message = "用户名长度必须在50个字符以内")
  private String loginAccount;


//  @NotNull(message = "旧密码不能为空")
//  @Size(max = 50, message = "用户旧密码长度不能超过50个字符")
  private String oldPassword;

//  @NotNull(message = "新密码不能为空")
//  @Size(max = 50, message = "用户新密码长度不能超过50个字符")
  private String newPassword;

}
