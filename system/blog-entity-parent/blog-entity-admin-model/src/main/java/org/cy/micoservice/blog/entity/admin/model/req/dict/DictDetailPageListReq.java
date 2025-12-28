package org.cy.micoservice.blog.entity.admin.model.req.dict;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/3/11
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class DictDetailPageListReq extends BasePageReq implements Serializable {

	@Serial
	private static final long serialVersionUID = 1290392551670370158L;

	@NotNull(message = "字典id不能为空")
	private Long dictId;

	private Integer type;

	private String name;

	private String remark;
}
