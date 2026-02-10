package org.cy.micoservice.app.entity.infra.console.model.resp.sys.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysRole;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class SysRoleResp extends SysRole {
  @Serial
  private static final long serialVersionUID = -3950491727801354218L;

//	@JsonSerialize(using = ToStringSerializer.class)
//	private Long roleTypeParentId;
//
//	private String roleTypeName;

}
