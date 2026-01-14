package org.cy.micoservice.blog.admin.api.service;


import org.cy.micoservice.blog.entity.admin.model.dto.acl.AclDto;
import org.cy.micoservice.blog.entity.admin.model.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.blog.entity.admin.model.dto.org.OrgLevelDto;

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


	List<AclModuleDto> roleAclTree(Long roleId);

	List<AclModuleDto> userAclTree(Long userId);

	List<AclModuleDto> aclListToTree(List<AclDto> aclDtoList);

}