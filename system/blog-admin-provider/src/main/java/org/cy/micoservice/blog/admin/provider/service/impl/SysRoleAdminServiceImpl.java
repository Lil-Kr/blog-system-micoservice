package org.cy.micoservice.blog.admin.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.admin.provider.dao.SysRoleAdminMapper;
import org.cy.micoservice.blog.admin.provider.dao.SysAdminMapper;
import org.cy.micoservice.blog.admin.provider.service.RbacCacheService;
import org.cy.micoservice.blog.admin.provider.service.MessageLangService;
import org.cy.micoservice.blog.admin.provider.service.SysRoleAdminService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.exception.BizException;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysRoleAdmin;
import org.cy.micoservice.blog.entity.admin.model.req.sys.roleuser.RoleAdminReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.admin.SysAdminResp;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.role.RoleAdminResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import static org.cy.micoservice.blog.admin.provider.common.constants.CommonConstants.LANG_ZH;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: role-admin relation service
 */
@Service
@Slf4j
public class SysRoleAdminServiceImpl extends ServiceImpl<SysRoleAdminMapper, SysRoleAdmin> implements SysRoleAdminService {

	@Autowired
	private MessageLangService msgService;

	@Autowired
	private SysRoleAdminMapper roleAdminMapper;

	@Autowired
	private SysAdminMapper adminMapper;

	@Autowired
	private RbacCacheService rbacCacheService;

	/**
	 * 更新[角色-用户]信息
	 * @param req
	 * @return
	 */
	@Override
	public ApiResp<String> updateRoleAdmins(RoleAdminReq req) {
		/**
		 * 查询当前角色id已分配的用户信息
		 */
		List<Long> originUserIdList = roleAdminMapper.selectAdminIdListByRoleId(req.getRoleId());
		if (CollectionUtils.isEmpty(originUserIdList)) {
			return ApiResp.warning(msgService.getMessage(LANG_ZH, "sys.role.user.resp.msg1"));
		}

		/**
		 * 将需要修改的角色id转为 -> list
		 */
		List<Long> userIdList = req.getAdminIdList();
		if (CollectionUtils.isEmpty(userIdList)) {
			return ApiResp.warning(msgService.getMessage(LANG_ZH, "sys.role.user.resp.msg2"));
		}

		/**
		 * 待修改的用户id列表 与 原来的用户id列表做数量对比
		 */
		if (originUserIdList.size() == userIdList.size()) {
			Set<Long> originUserIdSet = Sets.newHashSet(originUserIdList);
			Set<Long> userIdSet = Sets.newHashSet(userIdList);
			originUserIdSet.removeAll(userIdSet);
			if (CollectionUtils.isEmpty(originUserIdSet)) {
				return ApiResp.warning(msgService.getMessage(LANG_ZH, "sys.role.user.resp.msg3"));
			}
		}

		/**
		 * 更新[角色-用户]信息
		 */
		this.updateRoleUsers(req.getRoleId(), userIdList, req.getAdminId());

		/**
		 * 缓存失效: 用户的权限点失效
		 */
		rbacCacheService.invalidAdminAclCache(userIdList);
		return ApiResp.success(msgService.getMessage(LANG_ZH, "sys.role.user.resp.msg4"));
	}

	/**
	 * 更新[角色-用户]信息
	 * @param roleId
	 * @param adminIdList
	 */
	@Transactional
	public void updateRoleUsers(Long roleId, List<Long> adminIdList, Long adminId) {
		if (CollectionUtils.isEmpty(adminIdList)) {
			return;
		}
		// 删除旧的 [角色-用户] 对应关系数据
		QueryWrapper<SysRoleAdmin> wrapper = new QueryWrapper<>();
		wrapper.eq("role_id",roleId);
		int delete = roleAdminMapper.delete(wrapper);
		if (delete < 1) {
			throw new BizException(msgService.getMessage(LANG_ZH, "sys.role.user.resp.msg5"));
		}

		Date currentTime = DateUtil.dateTimeNow();
		List<SysRoleAdmin> roleUsers = adminIdList.stream()
			.map(userId -> SysRoleAdmin.builder()
				.surrogateId(IdWorker.getSnowFlakeId())
				.roleId(roleId)
				.userId(userId)
				.operateIp("127.0.0.1")
				.operator(adminId)
				.createTime(currentTime)
				.updateTime(currentTime)
				.build())
			.collect(Collectors.toList());

		// 批量更新角色-用户信息
		this.saveBatch(roleUsers);
	}

	/**
	 * 角色用户[待选列表-已选列表]
	 * @param req
	 * @return
	 */
	@Override
	public ApiResp<RoleAdminResp> roleAdminList(RoleAdminReq req) {
		/**
		 * query user id list by roleId
		 */
		List<Long> userIdList = roleAdminMapper.selectAdminIdListByRoleId(req.getRoleId());

		/**
		 * 查询角色对应分配的用户信息
		 * 用于已选列表
		 */
		List<SysAdminResp> roleUserSelectList = CollectionUtils.isEmpty(userIdList) ? Lists.newArrayList() : adminMapper.selectAdminListByIds(userIdList);

		/**
		 * 查询所有用户信息, 与 已选列表互斥
		 * 当前角色从未分配用户时, 返回整个用户列表
		 */
		List<SysAdminResp> roleUserAllList = adminMapper.selectAdminList();
		if (CollectionUtils.isEmpty(userIdList)) {
			roleUserAllList.removeIf(item -> roleUserSelectList.stream().anyMatch(i -> Objects.equals(i.getId(), item.getId())));
		}

		RoleAdminResp build = RoleAdminResp.builder()
			.selectedUserList(roleUserSelectList)
			.unSelectedUserList(roleUserAllList)
			.build();
		return ApiResp.success(build);
	}
}
