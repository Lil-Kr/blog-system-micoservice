package org.cy.micoservice.blog.entity.admin.model.req.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2024/3/14
 * @Description:
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserLoginAdminReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -1406372310723223433L;

  public interface AdminLogin {};

  private String token;

  @NotBlank(groups = {AdminLogin.class}, message = "账号不能为空")
  private String account;

  @NotBlank(groups = {AdminLogin.class}, message = "密码不能为空")
  private String password;

  private String email;

  public String getAccount() {
    return account;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
