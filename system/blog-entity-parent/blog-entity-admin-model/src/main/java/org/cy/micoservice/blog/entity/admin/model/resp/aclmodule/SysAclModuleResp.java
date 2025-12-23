package org.cy.micoservice.blog.entity.admin.model.resp.aclmodule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.cy.micoservice.blog.entity.admin.model.entity.SysAclModule;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class SysAclModuleResp extends SysAclModule implements Serializable {

	@Serial
	private static final long serialVersionUID = -8563288121083628988L;

}
