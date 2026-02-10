package org.cy.micoservice.app.entity.infra.console.model.req.sys.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.app.common.utils.CheckUtil;
import org.cy.micoservice.app.entity.base.model.api.BaseReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2024/3/17
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminRegisterReq extends BaseReq {

  @Serial
  private static final long serialVersionUID = -6375249917709496943L;

  /**
   * 登录账号
   */
  @NotBlank(message = "账号不能为空")
  @Size(min = 5, max = 20, message = "账号长度范围在 5~20 个字符之间")
  @Pattern(regexp = CheckUtil.ACCOUNT_REGEXP, message = "账号必须以下划线字母数字开头")
  private String account;

  /**
   * 用户名
   */
  @NotBlank(message = "昵称不能为空")
  @Size(min = 5, max = 20, message = "昵称长度过长, 必须在20个字符以内")
  private String userName;

  /**
   * 邮箱
   */
  @NotBlank(message = "邮箱不能为空")
  @Pattern(regexp = CheckUtil.EMAIL_REGEXP, message = "邮件不合法")
  private String email;

}
