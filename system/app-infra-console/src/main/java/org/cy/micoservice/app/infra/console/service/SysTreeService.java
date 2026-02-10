package org.cy.micoservice.app.infra.console.service;

import org.cy.micoservice.app.infra.facade.dto.acl.AclDto;
import org.cy.micoservice.app.infra.facade.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.app.infra.facade.dto.org.OrgLevelDto;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.role.RoleSaveReq;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
public interface SysTreeService {

	List<OrgLevelDto> orgTree();

//	List<OrgLevelDto> orgListToTree(List<OrgLevelDto> dtoList);

	List<AclModuleDto> aclModuleTree();


	List<AclModuleDto> roleAclTree(RoleSaveReq req);

	List<AclModuleDto> userAclTree(Long userId);

	List<AclModuleDto> aclListToTree(List<AclDto> aclDtoList);

}