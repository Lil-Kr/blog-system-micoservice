package org.cy.micoservice.blog.entity.admin.model.req.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.base.model.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/3/12
 * @Description: RoleListPageReq
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class RoleListPageReq extends BasePageReq {

	@Serial
	private static final long serialVersionUID = 171763455596524084L;

	private Long surrogateId;

	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 角色类型, 1超级管理员, 2管理员, 3.普通角色
	 */
	private Integer type;


	/**
	 * 状态, 0正常, 1冻结
	 */
	private Integer status;

	private String remark;
}
