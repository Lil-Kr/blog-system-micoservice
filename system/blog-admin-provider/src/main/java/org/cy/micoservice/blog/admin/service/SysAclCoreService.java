package org.cy.micoservice.blog.admin.service;


import org.cy.micoservice.blog.entity.admin.model.entity.SysAcl;

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
	List<SysAcl> getCurrentUserAclList();

	/**
	 * 获取[角色-权限]列表
	 */
	List<SysAcl> getRoleAclList(Long roleSurrogateId);

	List<SysAcl> getUserAclList(Long userId);

	List<SysAcl> getUserAclList(Long userId, Integer type);

	boolean hasUrlAcl(String url);
}
