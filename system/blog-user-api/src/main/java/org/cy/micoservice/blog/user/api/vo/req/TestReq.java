package org.cy.micoservice.blog.user.api.vo.req;

import org.cy.micoservice.blog.common.utils.CheckUtil;
import lombok.Data;
import lombok.ToString;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
@ToString
@Data
public class TestReq {

  @NotBlank(groups = {Default.class}, message = "用户账号不能为空")
  @Size(min = 5, max = 20, message = "账号长度范围在 5~20 个字符之间")
  @Pattern(regexp = CheckUtil.ACCOUNT_REGEXP, message = "账号必须以字母,数字,下划线 开头")
  private String name;

  @NotNull(message = "用户所在组织不能为空")
  private Long id;
}
