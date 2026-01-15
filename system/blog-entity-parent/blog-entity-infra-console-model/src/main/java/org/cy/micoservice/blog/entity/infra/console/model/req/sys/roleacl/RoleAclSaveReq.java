package org.cy.micoservice.blog.entity.infra.console.model.req.sys.roleacl;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.api.BaseReq;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/31
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleAclSaveReq extends BaseReq implements Serializable {

	@Serial
	private static final long serialVersionUID = -3141198516023396451L;

	public interface GroupUpdate {};

	public interface GroupUpdateRoleAcls {};
	/**
	 * 角色-权限id 唯一主键
	 */
	private Long surrogateId;

	/**
	 * 角色id
	 */
	@NotNull(groups = {GroupUpdateRoleAcls.class}, message = "角色id不为空")
	private Long roleId;

	/**
	 * 单个
	 */
	@NotNull(message = "权限aclId不为空")
	private Long aclId;

	/**
	 * 待更新的权限点id list
	 *
	 */
	@NotEmpty(groups = GroupUpdateRoleAcls.class, message = "aclIdList不能为空")
	private List<Long> aclIdList;
}
