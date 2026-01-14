package org.cy.micoservice.blog.admin.facade.interfaces;

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
public interface SysDictDetailFacade {

	ApiResp<String> addDetail(SaveDictDetailReq req);

	ApiResp<String> editDetail(SaveDictDetailReq req);

	ApiResp<String> deleteDetail(Long surrogateId);

	PageResult<SysDictDetailResp> pageDictDetailList(DictDetailPageListReq req);

	SysDictDetail get(SaveDictDetailReq req);
}