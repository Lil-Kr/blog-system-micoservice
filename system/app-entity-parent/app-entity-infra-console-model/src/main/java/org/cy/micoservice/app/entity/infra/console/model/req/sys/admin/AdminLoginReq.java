package org.cy.micoservice.app.entity.infra.console.model.req.sys.admin;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2024/3/14
 * @Description:
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminLoginReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -1406372310723223433L;

  public interface AdminLogin {};

  private String token;

  @NotBlank(groups = {AdminLogin.class}, message = "账号不能为空")
  private String account;

  @NotBlank(groups = {AdminLogin.class}, message = "密码不能为空")
  private String password;

  private String email;
}
