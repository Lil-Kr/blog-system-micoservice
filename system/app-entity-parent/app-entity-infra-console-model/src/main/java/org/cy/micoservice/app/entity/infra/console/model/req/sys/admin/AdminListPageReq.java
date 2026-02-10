package org.cy.micoservice.app.entity.infra.console.model.req.sys.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.app.common.enums.infra.AdminStatueEnum;
import org.cy.micoservice.app.entity.base.model.api.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/3/7
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminListPageReq extends BasePageReq {

	@Serial
	private static final long serialVersionUID = 6736712595365235015L;

	private Long id;

	/**
	 * 姓名
	 */
	private String userName;

	/**
	 * 电话
	 */
	private String telephone;

	/**
	 * 邮箱
	 */
	private String mail;

	/**
	 * 用户所在组织id
	 */
	private Long orgId;

	/**
	 * 状态, 0正常, 1冻结, 2: 删除
	 * @see AdminStatueEnum
	 */
	private Integer status = 0;

	/**
	 * 备注
	 */
	private String remark;

}
