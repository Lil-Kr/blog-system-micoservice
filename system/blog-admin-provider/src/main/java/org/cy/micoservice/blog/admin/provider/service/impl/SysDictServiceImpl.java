package org.cy.micoservice.blog.admin.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.cy.micoservice.blog.admin.provider.dao.SysDictDetailMapper;
import org.cy.micoservice.blog.admin.provider.dao.SysDictMapper;
import org.cy.micoservice.blog.admin.provider.service.RbacCacheService;
import org.cy.micoservice.blog.admin.provider.service.SysDictService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.common.enums.biz.DeleteStatusEnum;
import org.cy.micoservice.blog.common.exception.BizException;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.cy.micoservice.blog.common.utils.IdWorker;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDict;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictDetailReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictSaveReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictDetailResp;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.cy.micoservice.blog.admin.provider.common.constants.CommonConstants.BUS_CREATE;
import static org.cy.micoservice.blog.admin.provider.common.constants.CommonConstants.BUS_EDIT;
import static org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum.*;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: dict service
 */
@Service
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

	@Autowired
	private SysDictMapper dictMapper;

	@Autowired
	private SysDictDetailMapper dictDetailMapper;

	@Autowired
	private RbacCacheService rbacCacheService;

	/**
	 * 新增数据字典分类
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResp<String> add(DictSaveReq req) {
		if (checkExist(req.getSurrogateId(), req.getName())) {
			return ApiResp.failure(INFO_EXIST);
		}

		Long surrogateId = IdWorker.getSnowFlakeId(); // surrogateId
		Date currentTime = DateUtil.dateTimeNow();// 当前时间

		SysDict dict = SysDict.builder()
			.surrogateId(surrogateId)
			.name(req.getName())
			.remark(req.getRemark())
			.deleted(0) // default 0
			.operator(req.getAdminId())
			.operateIp("127.0.0.1")
			.createTime(currentTime)
			.updateTime(currentTime)
			.build();

		int insert = dictMapper.insert(dict);
		if (insert >= 1) {
			rbacCacheService.updateDictCache(dict.getSurrogateId(), dict, BUS_CREATE);
			return ApiResp.success();
		} else {
			return ApiResp.failure(ADD_ERROR);
		}
	}

	/**
	 * 检查是否有相同类型的数据字典类别
	 * @param surrogateId
	 * @param name
	 */
	protected boolean checkExist(Long surrogateId, String name) {
		QueryWrapper<SysDict> query = new QueryWrapper<>();
		if (Objects.nonNull(surrogateId)) {
			query.eq("surrogate_id", surrogateId);
		}
		if (Objects.nonNull(name)) {
			query.eq("name", name);
		}
		Long count = dictMapper.selectCount(query);
		if (count >= 1) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public ApiResp<String> edit(DictSaveReq req) {
		QueryWrapper<SysDict> query = new QueryWrapper<>();
		query.eq("surrogate_id", req.getSurrogateId());
		SysDict before = dictMapper.selectOne(query);
		if (Objects.isNull(before)) {
			return ApiResp.failure("待更新的数据字典信息不存在");
		}

		SysDict after = SysDict.builder()
			.surrogateId(before.getSurrogateId())
			.name(req.getName())
			.remark(req.getRemark())
			.deleted(DeleteStatusEnum.ACTIVE.getCode())
			.operateIp("127.0.0.1")
			.operator(req.getAdminId())
			.updateTime(DateUtil.dateTimeNow())
			.build();

		try {
			int update = dictMapper.update(after, query);
			if (update >= 1) {
				rbacCacheService.updateDictCache(after.getSurrogateId(), after, BUS_EDIT);
				return ApiResp.success("修改数据字典信息成功");
			}else {
				return ApiResp.failure("修改数据字典信息失败");
			}
		} catch (Exception e) {
			throw new BizException(INFO_EXIST);
		}
	}

	/**
	 * 数据字典类型列表列表
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageResult<SysDictResp> listAll() {
		return null;
	}

	/**
	 * 获取明细组
	 * @param req
	 * @return
	 */
	@Override
	public ApiResp<SysDictResp> dictDetailList(DictDetailReq req) {
		SysDictResp dict = this.getDict(req.getDictSurrogateId());
		if (Objects.isNull(dict)) {
			return ApiResp.failure(INFO_NOT_EXIST);
		}

		List<SysDictDetailResp> dictDetailList = dictDetailMapper.getDictDetailListByParentId(req.getDictSurrogateId());
		if (CollectionUtils.isEmpty(dictDetailList)) {
			return ApiResp.failure(INFO_NOT_EXIST);
		}

		dict.setDictDetailList(dictDetailList);
		return ApiResp.success(dict);
	}

	@Override
	public SysDictResp getDict(Long surrogateId) {
		return dictMapper.getDict(surrogateId);
	}

	/**
	 * 分页查询字典主信息
	 * @param req
	 * @return
	 */
	@Override
	public PageResult<SysDictResp> pageList(DictListPageReq req) {
		List<SysDictResp> pageList = dictMapper.pageDictList(req);
		Integer count = dictMapper.countPageDict(req);
		if (CollectionUtils.isNotEmpty(pageList)) {
			return new PageResult<>(pageList, count);
		}else {
			return PageResult.emptyPage();
		}
	}

	@Override
	public ApiResp<String> delete(Long surrogateId) {
		QueryWrapper<SysDictDetail> wrapper = new QueryWrapper<>();
		wrapper.eq("parent_id", surrogateId);
		Long count = dictDetailMapper.selectCount(wrapper);
		if (count >= 1) {
			return ApiResp.failure("该字典下存在明细, 删除失败");
		}

		QueryWrapper<SysDict> deleteWrapper = new QueryWrapper<>();
		deleteWrapper.eq("surrogate_id", surrogateId);
		int delete = dictMapper.delete(deleteWrapper);
		if (delete >= 1) {
			// 移除缓存
			rbacCacheService.removeDicCache(surrogateId);
			return ApiResp.success();
		}
		return ApiResp.failure(DEL_ERROR);
	}

	/**
	 * 查询字典明细 -> map
	 * @return
	 */
	@Override
	public ApiResp<Map<String, List<SysDictDetailResp>>> dictDetailMapping() {
		List<SysDictDetailResp> dictDetailTree = dictDetailMapper.dictDetailList();
		Map<String, List<SysDictDetailResp>> collect = dictDetailTree.stream().collect(Collectors.groupingBy(SysDictDetailResp::getParentName));
		return ApiResp.success(MapUtils.isEmpty(collect) ? Maps.newHashMap() : collect);
	}
}
