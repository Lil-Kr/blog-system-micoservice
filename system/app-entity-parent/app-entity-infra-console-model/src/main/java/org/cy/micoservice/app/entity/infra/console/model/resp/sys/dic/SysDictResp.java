package org.cy.micoservice.app.entity.infra.console.model.resp.sys.dic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysDict;

import java.io.Serial;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: dict
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class SysDictResp extends SysDict {
	@Serial
	private static final long serialVersionUID = 1219364284711528199L;

	private List<SysDictDetailResp> dictDetailList;
}