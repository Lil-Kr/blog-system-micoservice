package org.cy.micoservice.blog.entity.admin.model.req.roleacl;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/31
 * @Description:
 */
@Data
@ToString
public class RoleAclSaveReq implements Serializable {

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
