package org.cy.micoservice.blog.admin.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.admin.api.service.SysDictService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysDictFacade;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictDetailReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictSaveReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictDetailResp;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictResp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description: dict service
 */
@Slf4j
@Service
public class SysDictServiceImpl implements SysDictService {

	@DubboReference(check = false)
	private SysDictFacade dictFacade;

	@Override
	public ApiResp<String> add(DictSaveReq req) {
		return dictFacade.add(req);
	}

	@Override
	public ApiResp<String> edit(DictSaveReq req) {
		return dictFacade.edit(req);
	}

	@Override
	public PageResult<SysDictResp> listAll() {
		return dictFacade.listAll();
	}

	@Override
	public ApiResp<SysDictResp> dictDetailList(DictDetailReq req) {
		return dictFacade.dictDetailList(req);
	}

	@Override
	public SysDictResp getDict(Long surrogateId) {
		return dictFacade.getDict(surrogateId);
	}

	@Override
	public PageResult<SysDictResp> pageList(DictListPageReq req) {
		return dictFacade.pageList(req);
	}

	@Override
	public ApiResp<String> delete(Long surrogateId) {
		return dictFacade.delete(surrogateId);
	}

	@Override
	public ApiResp<Map<String, List<SysDictDetailResp>>> dictDetailMapping() {
		return dictFacade.dictDetailMapping();
	}
}
