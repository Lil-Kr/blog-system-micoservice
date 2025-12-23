package org.cy.micoservice.blog.entity.admin.model.req.dict;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/3/12
 * @Description:
 */
@Data
@ToString
public class DictSaveReq implements Serializable {

	@Serial
	private static final long serialVersionUID = 1191390378652890370L;

	public interface DictAddGroup {};
	public interface DictEditGroup {};
	public interface DictDeleteGroup {};

	/**
	 * 自增主键
	 */
	private Long id;

	/**
	 * 数据字典id唯一主键
	 */
	@NotNull(groups = {DictEditGroup.class}, message = "字典名不能为空")
	private Long surrogateId;

	/**
	 * 数据字典名称
	 */
	@NotBlank(groups = {DictAddGroup.class, DictEditGroup.class}, message = "字典名不能为空")
	@Size(groups = {DictAddGroup.class, DictEditGroup.class}, min = 2, max = 20, message = "数据字典名长度必须在2~20个字符之间")
	private String name;

	/**
	 * 备注
	 */
	@Size(max = 100, message = "数据字典备注长度必须在100个字符以内")
	private String remark;
}
