package org.cy.micoservice.blog.admin.provider.service;

import org.cy.micoservice.blog.entity.admin.model.entity.blog.BlogLabel;
import org.cy.micoservice.blog.entity.admin.model.entity.blog.BlogTopic;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAcl;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDict;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.resp.blog.BlogCategoryResp;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/28
 * @Description: cache service
 */
public interface RbacCacheService {

	/** ================= admin-user cache by token ============== **/
	void setAdminTokenCache(String token, SysAdmin user);

	SysAdmin getAdminTokenCache(String token);

	void removeAdminTokenCache(String token);

	/** ================= admin-user cache by id, use for data transform case  ================= **/
	void initAdminIdCache(List<SysAdmin> list);
	SysAdmin getAdminIdCache(Long id);
	void setAdminIdCache(Long id, SysAdmin user);
	void removeAdminIdCache(Long id);

	/** ================= blog label ================= **/
	List<BlogLabel> getLabelListCache(String key);

	BlogLabel getLabelCache(Long surrogateId);

	void updateLabelCache(String key, String sign, BlogLabel blogLabel);

	// 刷新缓存
	void saveLabelCache(String key, List<BlogLabel> labelList);

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
	 * ================================== blog category ===============================
	 */
	void saveBlogCategoryCache(List<BlogCategoryResp> categoryList);

	void updateBlogCategoryCache(String key, BlogCategoryResp categoryVO, String sign);

	List<BlogCategoryResp> getBlogCategoryListCache(String key);

	BlogCategoryResp getBlogCategoryCache(Long surrogateId);

	/**
	 * ================================== blog topic ===============================
	 */
	void saveBlogTopicCache(List<BlogTopic> list);

	void updateBlogTopicCache(String key, BlogTopic topic, String sign, Long delId);

	BlogTopic getTopicCache(Long surrogateId);

	List<BlogTopic> getTopicListCache(String key);

	/**
	 * ================================== sys admin-user acl ===============================
	 */
	void saveAdminAclCache(Long surrogateId, List<SysAcl> aclList);

	List<SysAcl> getAdminAclListCache(Long userId);

	void invalidAdminAclCache(List<Long> userIdList);

	void invalidAllAdminAclCache();
}
