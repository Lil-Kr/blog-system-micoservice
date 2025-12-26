package org.cy.micoservice.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.admin.common.holder.RequestHolder;
import org.cy.micoservice.blog.admin.dao.SysRoleUserMapper;
import org.cy.micoservice.blog.admin.dao.SysUserMapper;
import org.cy.micoservice.blog.admin.service.CacheService;
import org.cy.micoservice.blog.admin.service.MessageLangService;
import org.cy.micoservice.blog.admin.service.SysRoleUserService;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.exception.BizException;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.cy.micoservice.blog.entity.admin.model.entity.SysRoleUser;
import org.cy.micoservice.blog.entity.admin.model.req.roleuser.RoleUserReq;
import org.cy.micoservice.blog.entity.admin.model.resp.admin.SysUserResp;
import org.cy.micoservice.blog.entity.admin.model.resp.role.RoleUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.cy.micoservice.blog.admin.common.constants.CommonConstants.LANG_ZH;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: role-user relation service
 */
@Service
@Slf4j
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUser> implements SysRoleUserService {

	@Autowired
	private MessageLangService msgService;

	@Autowired
	private SysRoleUserMapper roleUserMapper;

	@Autowired
	private SysUserMapper userMapper;

	@Autowired
	private CacheService cacheService;

	/**
	 * 更新[角色-用户]信息
	 * @param req
	 * @return
	 */
	@Override
	public ApiResp<String> updateRoleUsers(RoleUserReq req) {
		/**
		 * 查询当前角色id已分配的用户信息
		 */
		List<Long> originUserIdList = roleUserMapper.selectUserIdListByRoleId(req.getRoleId());
		if (CollectionUtils.isEmpty(originUserIdList)) {
			return ApiResp.warning(msgService.getMessage(LANG_ZH, "sys.role.user.resp.msg1"));
		}

		/**
		 * 将需要修改的角色id转为 -> list
		 */
		List<Long> userIdList = req.getUserIdList();
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
		this.updateRoleUsers(req.getRoleId(), userIdList);

		/**
		 * 缓存失效: 用户的权限点失效
		 */
		cacheService.invalidUserAclCache(userIdList);
		return ApiResp.success(msgService.getMessage(LANG_ZH, "sys.role.user.resp.msg4"));
	}

	/**
	 * 更新[角色-用户]信息
	 * @param roleId
	 * @param userIdList
	 */
	@Transactional
	public void updateRoleUsers(Long roleId, List<Long> userIdList) {
		if (CollectionUtils.isEmpty(userIdList)) {
			return;
		}
		// 删除旧的 [角色-用户] 对应关系数据
		QueryWrapper<SysRoleUser> wrapper = new QueryWrapper<>();
		wrapper.eq("role_id",roleId);
		int delete = roleUserMapper.delete(wrapper);
		if (delete < 1) {
			throw new BizException(msgService.getMessage(LANG_ZH, "sys.role.user.resp.msg5"));
		}

		Date currentTime = DateUtil.dateTimeNow();
		List<SysRoleUser> roleUsers = userIdList.stream()
			.map(userId -> SysRoleUser.builder()
				.surrogateId(IdWorker.getSnowFlakeId())
				.roleId(roleId)
				.userId(userId)
				.operateIp("127.0.0.1")
				.operator(RequestHolder.getCurrentUser().getSurrogateId())
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
	public ApiResp<RoleUserResp> roleUserList(RoleUserReq req) {
		/**
		 * query user id list by roleId
		 */
		List<Long> userIdList = roleUserMapper.selectUserIdListByRoleId(req.getRoleId());

		/**
		 * 查询角色对应分配的用户信息
		 * 用于已选列表
		 */
		List<SysUserResp> roleUserSelectList = CollectionUtils.isEmpty(userIdList) ? Lists.newArrayList() : userMapper.selectUserListByIds(userIdList);

		/**
		 * 查询所有用户信息, 与 已选列表互斥
		 * 当前角色从未分配用户时, 返回整个用户列表
		 */
		List<SysUserResp> roleUserAllList = userMapper.selectUserList();
		if (CollectionUtils.isEmpty(userIdList)) {
			roleUserAllList.removeIf(item -> roleUserSelectList.stream().anyMatch(i -> Objects.equals(i.getSurrogateId(), item.getSurrogateId())));
		}

		RoleUserResp build = RoleUserResp.builder()
			.selectedUserList(roleUserSelectList)
			.unSelectedUserList(roleUserAllList)
			.build();
		return ApiResp.success(build);
	}
}
