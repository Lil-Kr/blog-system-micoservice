package org.cy.micoservice.blog.entity.infra.console.model.req.sys.org;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.base.model.api.BasePageReq;

import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/3/6
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class OrgPageReq extends BasePageReq {

	@Serial
	private static final long serialVersionUID = 8983160311730826125L;

	public interface GroupChildOrgList{};

	@NotNull(groups = {GroupChildOrgList.class}, message = "surrogateId不能为空")
	private Long surrogateId;

	private Long parentId;

	// org number
	private String number;

	// org name
	private String name;

	private Integer seq;

	private Integer status;
}
