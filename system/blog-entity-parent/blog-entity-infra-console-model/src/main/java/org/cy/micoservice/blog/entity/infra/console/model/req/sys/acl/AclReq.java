package org.cy.micoservice.blog.entity.infra.console.model.req.sys.acl;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cy.micoservice.blog.entity.base.model.api.BaseReq;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/3/21
 * @Description: AclReq
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AclReq extends BaseReq implements Serializable {

	@Serial
	private static final long serialVersionUID = 7429956937215319739L;

	public interface GroupAcls {}

	/**
	 * 自增主键
	 */
	private Long id;

	/**
	 * 权限id唯一主键
	 */
	@NotNull(groups = {GroupAcls.class}, message = "surrogateId不能为空")
	private Long aclId;

	/**
	 * 权限名
	 */
	@NotBlank(message = "name权限点名不能为空")
	@Size(min = 2,max = 20,message = "权限名长度为2~20个字符之间")
	private String name;

	/**
	 * 权限模块id
	 */
	@NotNull(message = "aclModuleId不能为空")
	private Long aclModuleId;

	/**
	 * 请求的url
	 */
	@Size(min = 1,max = 100,message = "权限点的url长度为 1~100 个字符之间")
	private String url;

	/**
	 * 1:菜单权限, 2按钮权限, 3其他
	 */
	@NotNull(message = "type 权限点类型不能为空")
	private Integer type;

	/**
	 * 菜单名
	 */
	@Size(min = 1,max = 50, message = "菜单名名范围 1~50 在之间")
	private String menuName;

	/**
	 * 菜单url, 路由url
	 */
	@Size(min = 1, max = 200, message = "权限点的url长度为 1~200 个字符之间")
	private String menuUrl;

	/**
	 *
	 */
	@Size(min = 1, max = 100, message = "按钮标识长度在 1~100 个字符之间")
	private String btnSign;


	/**
	 * 状态 0 正常 ,1 冻结
	 */
	@NotNull(message = "status状态不能为空")
	@Min(value = 0, message = "状态范围在 0~10 之间")
	@Max(value = 10, message = "状态范围在 0~10 之间")
	private Integer status;

	/**
	 * 排序
	 */
	@NotNull(message = "权限点顺序不能为空")
	private Integer seq;

	/**
	 * 备注
	 */
	@Size(min = 1,max = 100,message = "权限点备注长度为1~100个字符之间")
	private String remark;
}
