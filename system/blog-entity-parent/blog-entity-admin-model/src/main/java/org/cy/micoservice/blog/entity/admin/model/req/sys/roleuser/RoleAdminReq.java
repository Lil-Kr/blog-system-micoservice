package org.cy.micoservice.blog.entity.admin.model.req.sys.roleuser;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.api.BaseReq;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: Lil-K
 * @Date: 2020/12/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleAdminReq extends BaseReq implements Serializable {

  @Serial
  private static final long serialVersionUID = -4353137931192315210L;

  public interface GroupChangeRoleUsers {};

  public interface GroupRoleUserPageList {};

  /**
   * 自增主键
   */
  private Long id;

  /**
   * 角色-用户id唯一主键
   */
  private Long surrogateId;

  /**
   * 角色id
   */
  @NotNull(groups = {Default.class,GroupRoleUserPageList.class, GroupChangeRoleUsers.class}, message = "角色roleId不为空")
  private Long roleId;

  /**
   * 多个角色id, 用逗号分隔
   */
  @NotEmpty(groups = {GroupChangeRoleUsers.class}, message = "角色userId不为空")
  private List<Long> adminIdList;

  /**
   * 用户id
   */
  @NotNull(groups = {Default.class}, message = "用户id不为空")
  private Long adminId;
}
