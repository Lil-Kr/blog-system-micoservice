package org.cy.micoservice.blog.admin.facade.interfaces;


import org.cy.micoservice.blog.entity.admin.model.dto.acl.AclDto;
import org.cy.micoservice.blog.entity.admin.model.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.blog.entity.admin.model.dto.org.OrgLevelDto;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
public interface SysTreeFacade {

	List<OrgLevelDto> orgTree();

	List<AclModuleDto> aclModuleTree();

	List<AclModuleDto> roleAclTree(Long roleId);

	List<AclModuleDto> adminAclTree(Long userId);

	List<AclModuleDto> aclListToTree(List<AclDto> aclDtoList);

}