package org.cy.micoservice.blog.entity.infra.console.model.resp.sys.acl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.cy.micoservice.blog.entity.infra.console.model.entity.sys.SysAcl;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysAclResp extends SysAcl {

	@Serial
	private static final long serialVersionUID = 4566362872261208792L;

	private String aclModuleName;
	private String creatorName;
	private String operatorName;
	private String nickName;
	private String aclTypeName;

}
