package org.cy.micoservice.app.infra.console.service;

import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAcl;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAdmin;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysDict;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysDictDetail;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/28
 * @Description: cache service
 */
public interface RbacCacheService {

	/** ================= admin-user cache by token ============== **/
	void setUserTokenCache(String token, SysAdmin user);

	SysAdmin getUserTokenCache(String token);

	void removeUserTokenCache(String token);

	/** ================= admin-user cache by id, use for data transform case  ================= **/
	void initUserAdminIdCache(List<SysAdmin> list);
	SysAdmin getUserAdminIdCache(Long id);
	void setUserAdminIdCache(Long id, SysAdmin user);
	void removeUserAdminIdCache(Long id);

	/**
	 * ================================== dict ===============================
	 **/
	void saveDictDetailCache(List<SysDict> dictVOList, List<SysDictDetail> dictDetailList);

	SysDict getDictCache(Long surrogateId);

	SysDictDetail getDictDetailCache(Long surrogateId);

	void updateDictCache(Long keyDict, SysDict dict, String sign);

	void updateDictDetailCache(Long keyDictDetail, SysDictDetail dictDetail, String sign);

	void removeDicCache(Long key);

	void removeDicDetailCache(Long key);

	/**
	 * ================================== sys admin-user acl ===============================
	 */
	void saveUserAclCache(Long surrogateId, List<SysAcl> aclList);

	List<SysAcl> getAdminAclListCache(Long userId);

	void invalidUserAclCache(List<Long> userIdList);

	void invalidAllUserAclCache();
}
