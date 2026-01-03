package org.cy.micoservice.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.admin.common.holder.RequestHolder;
import org.cy.micoservice.blog.admin.dao.BlogLabelMapper;
import org.cy.micoservice.blog.admin.pojo.dto.blog.BlogLabelDTO;
import org.cy.micoservice.blog.admin.pojo.entity.blog.BlogLabel;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelListReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelPageReq;
import org.cy.micoservice.blog.admin.pojo.req.blog.label.BlogLabelReq;
import org.cy.micoservice.blog.admin.pojo.resp.blog.BlogLabelResp;
import org.cy.micoservice.blog.admin.service.BlogLabelService;
import org.cy.micoservice.blog.admin.service.CacheService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.common.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.cy.micoservice.blog.admin.common.constants.CommonConstants.*;
import static org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum.*;

/**
 * @Author: Lil-K
 * @since 2024-03-31
 */
@Service
public class BlogLabelServiceImpl implements BlogLabelService {

	@Autowired
	private BlogLabelMapper blogLabelMapper;

	@Autowired
	private CacheService cacheService;

	@Override
	public PageResult<BlogLabelResp> pageList(BlogLabelPageReq req) {
		List<BlogLabelResp> pageList = blogLabelMapper.pageList(req);
		Integer count = blogLabelMapper.getCountByList(req);
		if (CollectionUtils.isEmpty(pageList)) {
			return PageResult.emptyPage();
		}else {
			return new PageResult<>(pageList, count);
		}
	}

	@Override
	public PageResult<BlogLabel> list(BlogLabelListReq req) {
		/**
		 * 先查询缓存, 在查询列表
		 */
		List<BlogLabel> labelList = cacheService.getLabelListCache(CACHE_KEY_BLOG_LABEL_LIST);
		if (CollectionUtils.isEmpty(labelList)) {
			labelList = blogLabelMapper.labelList(req);
			cacheService.saveLabelCache(CACHE_KEY_BLOG_LABEL_LIST, labelList);
		}

		if (CollectionUtils.isEmpty(labelList)) {
			return PageResult.emptyPage();
		}
		return new PageResult<>(labelList, labelList.size());
	}

	@Override
	public ApiResp<String> add(BlogLabelReq req) {
		BlogLabel saveEntity = BlogLabelDTO.convertSaveLabelReq(req);
		int add = blogLabelMapper.insert(saveEntity);
		if (add < 1) {
			return ApiResp.failure(ADD_ERROR);
		}

		// update cache
		cacheService.updateLabelCache(CACHE_KEY_BLOG_LABEL_LIST, BUS_CREATE, saveEntity);
		return ApiResp.success();
	}

	@Override
	public ApiResp<String> edit(BlogLabelReq req) {
		QueryWrapper<BlogLabel> query = new QueryWrapper<>();
		query.eq("surrogate_id", req.getSurrogateId());
		BlogLabel before = blogLabelMapper.selectOne(query);
		if (Objects.isNull(before)) {
			return ApiResp.failure(INFO_NOT_EXIST);
		}

		req.setUpdateTime(DateUtil.dateTimeNow());
		req.setOperator(RequestHolder.getCurrentUser().getSurrogateId());
		Integer count = blogLabelMapper.editBySurrogateId(req);

		if (count < 1) {
			return ApiResp.failure(UPDATE_ERROR);
		}

		// update cache
		BeanUtils.copyProperties(req, before);
		cacheService.updateLabelCache(CACHE_KEY_BLOG_LABEL_LIST, BUS_EDIT, before);
		return ApiResp.success();
	}

	@Override
	public ApiResp<String> delete(BlogLabelReq req) {
		int count = blogLabelMapper.deleteBySurrogateId(req.getSurrogateId());
		if (count < 1) {
			return ApiResp.failure(OPERATE_ERROR);
		}
		cacheService.updateLabelCache(CACHE_KEY_BLOG_LABEL_LIST, BUS_DELETE, new BlogLabel());
		return ApiResp.success();
	}

	@Override
	public ApiResp<String> deleteBatch(BlogLabelReq req) {
		Integer count = blogLabelMapper.deleteBatch(req.getSurrogateIds());
		if (count < 1) {
			return ApiResp.failure(OPERATE_ERROR);
		}
		return ApiResp.success();
	}
}
