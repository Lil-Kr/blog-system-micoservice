package org.cy.micoservice.app.entity.infra.console.model.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.app.entity.base.model.api.BaseEntity;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/3/8
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_acl")
public class SysAcl extends BaseEntity {

	@Serial
	private static final long serialVersionUID = 4265979647827463721L;

	/**
	 * 自增主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 权限id唯一主键
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long aclId;

	/**
	 * 权限编码
	 */
	private String number;

	/**
	 * 权限名
	 */
	private String name;

	/**
	 * 权限模块id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long aclModuleId;

	/**
	 * 请求的url
	 */
	private String url;

	/**
	 * 1:菜单权限, 2按钮, 3其他
	 */
	private Integer type;

	/**
	 * 菜单名
	 */
	private String menuName;

	/**
	 * 菜单url, 对应路由
	 */
	private String menuUrl;

	/**
	 * 按钮权限标识
	 */
	private String btnSign;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 排序
	 */
	private Integer seq;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 操作ip
	 */
	private String operateIp;
}
