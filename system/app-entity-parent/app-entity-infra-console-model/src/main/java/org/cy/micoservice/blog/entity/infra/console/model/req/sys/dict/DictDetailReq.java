package org.cy.micoservice.blog.entity.infra.console.model.req.sys.dict;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2025/3/9
 * @Description:
 */
@Data
@ToString
public class DictDetailReq implements Serializable {

	@Serial
	private static final long serialVersionUID = -5438362237626710032L;

	public interface GroupGetDictDetail{}

	private Long surrogateId;

	@NotNull(message = "surrogateId不能为空", groups = {GroupGetDictDetail.class})
	private Long dictSurrogateId;
}
