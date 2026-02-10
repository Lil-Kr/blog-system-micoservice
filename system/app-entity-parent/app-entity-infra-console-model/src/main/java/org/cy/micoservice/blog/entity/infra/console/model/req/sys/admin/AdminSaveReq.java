package org.cy.micoservice.blog.entity.infra.console.model.req.sys.admin;

import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.common.utils.CheckUtil;
import org.cy.micoservice.blog.entity.base.model.api.BaseReq;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;


@EqualsAndHashCode(callSuper = true)
@Data
public class AdminSaveReq extends BaseReq {

  @Serial
  private static final long serialVersionUID = -6784685540984804863L;

  public interface GroupGetAdminAcl {}
  public interface GroupAdminLogin {}
  public interface GroupAddAdmin {}
  public interface GroupEditAdmin {}

  @NotNull(groups = {GroupGetAdminAcl.class, Default.class, GroupEditAdmin.class}, message = "surrogateId不能为空")
  private Long id;

  /**
   * 用户注册账号
   */
  @NotNull(groups = {Default.class},message = "用户账号不能为空")
  @Size(min = 5, max = 20, message = "账号长度范围在 5~20 个字符之间")
  @Pattern(regexp = CheckUtil.ACCOUNT_REGEXP, message = "账号必须以下划线字母数字开头")
  private String account;

  /**
   * 用户姓名
   */
  @NotNull(groups = {Default.class, GroupAddAdmin.class}, message = "昵称不能为空")
  @Size(groups = {Default.class, GroupAddAdmin.class}, min = 2,max = 20, message = "昵称长度必须在2到20个字符之间")
  private String userName;

  /**
   * 用户手机号
   */
  @Size(groups = {Default.class, GroupAddAdmin.class}, min = 11, max = 11, message = "请输入正确的手机号")
  @Pattern(groups = {Default.class, GroupAddAdmin.class}, regexp = "^\\d{11}$", message = "手机号必须是11位数字")
  private String telephone;

  /**
   * 邮箱
   */
  @NotNull(groups = {Default.class, GroupAddAdmin.class},message = "用户邮箱不能为空")
  @Pattern(groups = {Default.class, GroupAddAdmin.class},regexp = CheckUtil.EMAIL_REGEXP, message = "邮箱格式不正确")
  private String email;

  /**
   * 用户所在组织id
   */
  @NotNull(groups = {Default.class, GroupAddAdmin.class},message = "用户所在组织不能为空")
  private Long orgId;

  /**
   * 状态, 0正常, 1异常, 2: 未知
   */
  @NotNull(message = "状态不能为空", groups = {GroupAddAdmin.class})
  @Min(groups = {Default.class, GroupAddAdmin.class}, value = 0, message = "状态类型范围0~2")
  @Max(groups = {Default.class, GroupAddAdmin.class}, value = 2, message = "状态类型范围0~2")
  private Integer status;

  /**
   * 备注
   */
  @Size(groups = {Default.class}, max = 100, message = "备注不能超过100个字符")
  private String remark;

  private MultipartFile avatarFile;

  private String avatar;
}