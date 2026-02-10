package org.cy.micoservice.app.infra.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysDictDetail;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.dict.DictDetailPageListReq;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.dict.SaveDictDetailReq;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.dic.SysDictDetailResp;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
public interface SysDictDetailService extends IService<SysDictDetail> {

	ApiResp<String> addDetail(SaveDictDetailReq param);

	ApiResp<String> editDetail(SaveDictDetailReq param);

	ApiResp<String> deleteDetail(Long surrogateId);

	PageResult<SysDictDetailResp> pageDictDetailList(DictDetailPageListReq req);

	SysDictDetail get(SaveDictDetailReq req);
}