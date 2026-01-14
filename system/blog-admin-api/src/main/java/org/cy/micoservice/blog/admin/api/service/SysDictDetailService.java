package org.cy.micoservice.blog.admin.api.service;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictDetailPageListReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.SaveDictDetailReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictDetailResp;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
public interface SysDictDetailService {

	ApiResp<String> addDetail(SaveDictDetailReq param);

	ApiResp<String> editDetail(SaveDictDetailReq param);

	ApiResp<String> deleteDetail(Long surrogateId);

	PageResult<SysDictDetailResp> pageDictDetailList(DictDetailPageListReq req);

	SysDictDetail get(SaveDictDetailReq req);
}