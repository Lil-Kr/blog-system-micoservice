package org.cy.micoservice.blog.entity.infra.console.model.resp.sys.dic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.infra.console.model.entity.sys.SysDictDetail;
import java.io.Serial;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
public class SysDictDetailResp extends SysDictDetail {

	@Serial
	private static final long serialVersionUID = -5914263986634565539L;

	private String parentName;
}