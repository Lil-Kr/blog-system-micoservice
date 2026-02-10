package org.cy.micoservice.app.entity.infra.console.model.resp.sys.aclmodule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAclModule;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class SysAclModuleResp extends SysAclModule implements Serializable {

	@Serial
	private static final long serialVersionUID = -8563288121083628988L;

}
