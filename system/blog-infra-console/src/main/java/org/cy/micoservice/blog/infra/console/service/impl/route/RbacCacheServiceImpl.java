package org.cy.micoservice.blog.infra.console.service.impl.route;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.entity.infra.console.model.entity.sys.SysAcl;
import org.cy.micoservice.blog.entity.infra.console.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.infra.console.model.entity.sys.SysDict;
import org.cy.micoservice.blog.entity.infra.console.model.entity.sys.SysDictDetail;
import org.cy.micoservice.blog.infra.console.service.RbacCacheService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/28
 * @Description:
 */
@Service
public class RbacCacheServiceImpl implements RbacCacheService, Serializable {

	private static final long serialVersionUID = -3794279386684757741L;

	/** ================= admin cache by token ============== **/
	private static Cache<String, SysAdmin> userCache = CacheBuilder.newBuilder().build();

	@Override
	public void setUserTokenCache(String token, SysAdmin user) {
		userCache.put(token, user);
	}

	@Override
	public SysAdmin getUserTokenCache(String token) {
		return userCache.getIfPresent(token);
	}

	@Override
	public void removeUserTokenCache(String token) {
		userCache.invalidate(token);
	}

	/** ================= admin cache by id ============== **/
	private static Cache<Long, SysAdmin> userAdminCache = CacheBuilder.newBuilder().build();

	@Override
	public void initUserAdminIdCache(List<SysAdmin> list) {
		list.parallelStream().forEach(user -> userAdminCache.put(user.getId(), user));
	}

	@Override
	public void setUserAdminIdCache(Long id, SysAdmin user) {
		userAdminCache.put(id, user);
	}

	@Override
	public SysAdmin getUserAdminIdCache(Long id) {
		return userAdminCache.getIfPresent(id);
	}

	@Override
	public void removeUserAdminIdCache(Long id) {
		userAdminCache.invalidate(id);
	}

	/**
	 * ========================== dict cache ==========================
	 */
	private static Cache<Long, SysDict> dictCache = CacheBuilder.newBuilder().build();
	private static Cache<Long, SysDictDetail> dictDetailCache = CacheBuilder.newBuilder().build();

	@Override
	public void saveDictDetailCache(List<SysDict> dictList, List<SysDictDetail> dictDetailList) {
		// 初始化数据字典主表
		dictList.parallelStream().forEach(item -> dictCache.put(item.getSurrogateId(), item));

		// 初始化数据字典明细表
		dictDetailList.parallelStream().forEach(item -> dictDetailCache.put(item.getSurrogateId(), item));
	}

	@Override
	public SysDict getDictCache(Long surrogateId) {
		return dictCache.getIfPresent(surrogateId);
	}

	@Override
	public SysDictDetail getDictDetailCache(Long surrogateId) {
		return dictDetailCache.getIfPresent(surrogateId);
	}

	@Override
	public void updateDictCache(Long keyDict, SysDict dict, String sign) {
		dictCache.put(keyDict, dict);
	}

	@Override
	public void updateDictDetailCache(Long keyDictDetail, SysDictDetail dictDetail, String sign) {
		dictDetailCache.put(keyDictDetail, dictDetail);
	}

	@Override
	public void removeDicCache(Long key) {
		dictCache.invalidate(key);
	}

	@Override
	public void removeDicDetailCache(Long key) {
		dictDetailCache.invalidate(key);
	}

	/**
	 * ================================== admin acl ===============================
	 */
	// <userId, List<SysAcl>>
	private static Cache<Long, List<SysAcl>> adminAclCache = CacheBuilder.newBuilder().build();

	/**
	 * 保存每个后台管理员的权限点
	 * @param surrogateId
	 * @param aclList
	 */
	@Override
	public void saveUserAclCache(Long surrogateId, List<SysAcl> aclList) {
		adminAclCache.put(surrogateId, aclList);
	}

	@Override
	public List<SysAcl> getAdminAclListCache(Long userId) {
		return adminAclCache.getIfPresent(userId);
	}

	/**
	 * 缓存失效
	 * @param userIdList
	 */
	@Override
	public void invalidUserAclCache(List<Long> userIdList) {
		if (CollectionUtils.isEmpty(userIdList)) {
			return;
		}
		userIdList.forEach(userId-> adminAclCache.invalidate(userId));
	}

	@Override
	public void invalidAllUserAclCache() {
		adminAclCache.invalidateAll();
	}
}
