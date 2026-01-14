package org.cy.micoservice.blog.admin.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.admin.api.service.SysDictDetailService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictDetailPageListReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.SaveDictDetailReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictDetailResp;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
@Service
@Slf4j
public class SysDictDetailServiceImpl implements SysDictDetailService {


	@Override
	public ApiResp<String> addDetail(SaveDictDetailReq param) {
		return null;
	}

	@Override
	public ApiResp<String> editDetail(SaveDictDetailReq param) {
		return null;
	}

	@Override
	public ApiResp<String> deleteDetail(Long surrogateId) {
		return null;
	}

	@Override
	public PageResult<SysDictDetailResp> pageDictDetailList(DictDetailPageListReq req) {
		return null;
	}

	@Override
	public SysDictDetail get(SaveDictDetailReq req) {
		return null;
	}
}
