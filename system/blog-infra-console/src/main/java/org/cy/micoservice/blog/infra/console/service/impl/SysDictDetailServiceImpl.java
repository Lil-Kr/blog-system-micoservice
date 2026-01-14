package org.cy.micoservice.blog.infra.console.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictDetailPageListReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.SaveDictDetailReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictDetailResp;
import org.cy.micoservice.blog.framework.id.starter.service.IdService;
import org.cy.micoservice.blog.infra.console.dao.rbac.SysDictDetailMapper;
import org.cy.micoservice.blog.infra.console.service.MessageLangService;
import org.cy.micoservice.blog.infra.console.service.RbacCacheService;
import org.cy.micoservice.blog.infra.console.service.SysDictDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.cy.micoservice.blog.common.constants.CommonConstants.LANG_ZH;
import static org.cy.micoservice.blog.infra.console.constant.CommonConstants.BUS_CREATE;
import static org.cy.micoservice.blog.infra.console.constant.CommonConstants.BUS_EDIT;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
@Service
@Slf4j
public class SysDictDetailServiceImpl extends ServiceImpl<SysDictDetailMapper, SysDictDetail> implements SysDictDetailService {

	@Autowired
	private SysDictDetailMapper dictDetailMapper;

	@Autowired
	private MessageLangService msgService;

	@Autowired
	private RbacCacheService rbacCacheService;

	@Autowired
	private IdService idService;

	@Override
	public ApiResp<String> addDetail(SaveDictDetailReq req) {
		if (checkDetailExist(req.getParentId(), req.getName(), req.getType())) {
			return ApiResp.failure(msgService.getMessage(LANG_ZH, "sys.dict.resp.msg1"));
		}

		Long surrogateId = idService.getId(); // surrogateId
		SysDictDetail dictDetail = SysDictDetail.builder()
			.surrogateId(surrogateId)
			.parentId(req.getParentId())
			.name(req.getName())
			.type(req.getType())
			.remark(req.getRemark())
			.build();
		int insert = dictDetailMapper.insert(dictDetail);
		if (insert >= 1) {
			rbacCacheService.updateDictDetailCache(dictDetail.getSurrogateId(), dictDetail, BUS_CREATE);
			return ApiResp.success(msgService.getMessage(LANG_ZH, "sys.dict.resp.msg2"));
		} else {
			return ApiResp.failure(msgService.getMessage(LANG_ZH, "sys.dict.resp.msg3"));
		}
	}

	/**
	 * 检查是否存在相同的明细名称
	 * @param parentId
	 * @param name
	 * @return
	 */
	protected boolean checkDetailExist(Long parentId, String name, Integer type) {
		QueryWrapper<SysDictDetail> query = new QueryWrapper<>();
		query.eq("parent_id", parentId);
		query.eq("name", name);
		query.eq("type", type);
		Long count = dictDetailMapper.selectCount(query);
		if (count >= 1) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 修改字典类型明细
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResp<String> editDetail(SaveDictDetailReq req) {
		if (checkDetailExist(req.getSurrogateId(), req.getName(), req.getType())) {
			return ApiResp.failure(msgService.getMessage(LANG_ZH, "sys.dict.resp.msg1"));
		}
		QueryWrapper<SysDictDetail> query = new QueryWrapper<>();
		query.eq("surrogate_id", req.getSurrogateId());
		SysDictDetail before = dictDetailMapper.selectOne(query);
		if (Objects.isNull(before)) {
			return ApiResp.failure(msgService.getMessage(LANG_ZH, "sys.dict.resp.msg4"));
		}

		SysDictDetail after = SysDictDetail.builder()
			.surrogateId(req.getSurrogateId())
			.parentId(req.getParentId())
			.type(req.getType())
			.name(req.getName())
			.remark(req.getRemark())
			.build();
		int update = dictDetailMapper.update(after, query);
		if (update >= 1) {
			rbacCacheService.updateDictDetailCache(after.getSurrogateId(), after, BUS_EDIT);
			return ApiResp.success(msgService.getMessage(LANG_ZH, "sys.dict.resp.msg5"));
		}else {
			return ApiResp.success(msgService.getMessage(LANG_ZH, "sys.dict.resp.msg6"));
		}
	}

	/**
	 * 删除字典明细
	 * @param surrogateId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResp<String> deleteDetail(Long surrogateId) {
		QueryWrapper<SysDictDetail> query = new QueryWrapper<>();
		query.eq("surrogate_id", surrogateId);
		int delete = dictDetailMapper.delete(query);
		if (delete >= 1) {
			// 更新缓存
			rbacCacheService.removeDicDetailCache(surrogateId);
			return ApiResp.success("删除字典明细信息成功");
		}else {
			return ApiResp.failure("删除字典明细信息失败");
		}
	}

	/**
	 * 根据字典主表分页查询字典明细数据
	 * @param req
	 * @return
	 */
	@Override
	public PageResult<SysDictDetailResp> pageDictDetailList(DictDetailPageListReq req) {
		List<SysDictDetailResp> pageList = dictDetailMapper.pageDictDetailListById(req);
		Integer count = dictDetailMapper.countPageDictDetail(req);
		if (CollectionUtils.isEmpty(pageList)) {
			return new PageResult<>(new ArrayList<>(0), 0);
		}else {
			return new PageResult<>(pageList, count);
		}
	}

	@Override
	public SysDictDetail get(SaveDictDetailReq req) {
		QueryWrapper<SysDictDetail> wrapper = new QueryWrapper<>();
		wrapper.eq("surrogate_id", req.getSurrogateId());
		SysDictDetail dictDetail = dictDetailMapper.selectOne(wrapper);
		if (Objects.isNull(dictDetail)) {
			return null;
		}
		return dictDetail;
	}
}
