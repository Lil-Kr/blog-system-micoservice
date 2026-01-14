package org.cy.micoservice.blog.admin.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.admin.api.service.SysTreeService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysTreeFacade;
import org.cy.micoservice.blog.entity.admin.model.dto.acl.AclDto;
import org.cy.micoservice.blog.entity.admin.model.dto.aclmodule.AclModuleDto;
import org.cy.micoservice.blog.entity.admin.model.dto.org.OrgLevelDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
@Slf4j
@Service
public class SysTreeServiceImpl implements SysTreeService {

	@DubboReference(check = false)
	private SysTreeFacade treeFacade;

	@Override
	public List<OrgLevelDto> orgTree() {
		return treeFacade.orgTree();
	}

	@Override
	public List<AclModuleDto> aclModuleTree() {
		return treeFacade.aclModuleTree();
	}

	@Override
	public List<AclModuleDto> roleAclTree(Long roleId) {
		return treeFacade.roleAclTree(roleId);
	}

	@Override
	public List<AclModuleDto> userAclTree(Long userId) {
		return treeFacade.adminAclTree(userId);
	}

	@Override
	public List<AclModuleDto> aclListToTree(List<AclDto> aclDtoList) {
		return treeFacade.aclListToTree(aclDtoList);
	}
}
