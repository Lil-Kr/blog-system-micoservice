package org.cy.micoservice.blog.entity.admin.model.req.dict;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/3/11
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class DictListPageReq extends BasePageReq {

	@Serial
	private static final long serialVersionUID = 9014802595650895084L;
	/**
	 * 数据字典明细名称
	 */
	private String name;

	/**
	 * 备注
	 */
	private String remark;
}
