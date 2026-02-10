package org.cy.micoservice.blog.entity.infra.console.model.resp.sys.role;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.cy.micoservice.blog.entity.infra.console.model.resp.sys.admin.SysAdminResp;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/15
 * @Description:
 */
@ToString
@Data
@Builder
public class RoleAdminResp implements Serializable {

	@Serial
	private static final long serialVersionUID = 1751006874425398424L;

	private List<SysAdminResp> selectedUserList;

	private List<SysAdminResp> unSelectedUserList;
}