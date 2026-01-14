package org.cy.micoservice.blog.admin.provider.service;


import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAcl;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/11
 * @Description:
 */
public interface SysAclCoreService {

	/**
	 * 获取当前用户所拥有的权限列表
	 */
	List<SysAcl> getCurrentAdminAclList();

	/**
	 * 获取[角色-权限]列表
	 */
	List<SysAcl> getRoleAclList(Long roleId);

	List<SysAcl> getAdminAclList(Long adminId);

	List<SysAcl> getAdminAclList(Long adminId, Integer type);

	boolean hasUrlAcl(String url);
}
