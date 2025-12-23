package org.cy.micoservice.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.req.dict.DictDetailPageListReq;
import org.cy.micoservice.blog.entity.admin.model.req.dict.SaveDictDetailReq;
import org.cy.micoservice.blog.entity.admin.model.resp.dic.SysDictDetailResp;

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