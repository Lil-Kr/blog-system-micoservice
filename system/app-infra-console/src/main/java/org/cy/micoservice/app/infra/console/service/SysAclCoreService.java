package org.cy.micoservice.app.infra.console.service;

import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAcl;

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
	// List<SysAcl> getCurrentUserAclList();

	List<SysAcl> getCurrentAdminAclList(Long adminId);

	/**
	 * 获取[角色-权限]列表
	 */
	List<SysAcl> getRoleAclList(Long roleSurrogateId);

	List<SysAcl> getAdminAclList(Long userId);

	List<SysAcl> getAdminAclList(Long userId, Integer type);

	boolean hasUrlAcl(String url);
}
