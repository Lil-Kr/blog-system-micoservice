package org.cy.micoservice.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.SysDict;
import org.cy.micoservice.blog.entity.admin.model.req.dict.DictDetailReq;
import org.cy.micoservice.blog.entity.admin.model.req.dict.DictListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.dict.DictSaveReq;
import org.cy.micoservice.blog.entity.admin.model.resp.dic.SysDictDetailResp;
import org.cy.micoservice.blog.entity.admin.model.resp.dic.SysDictResp;

import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/3/5
 * @Description:
 */
public interface SysDictService extends IService<SysDict> {

	ApiResp<String> add(DictSaveReq req);

	ApiResp<String> edit(DictSaveReq req);

	PageResult<SysDictResp> listAll();

	ApiResp<SysDictResp> dictDetailList(DictDetailReq req);

	SysDictResp getDict(Long surrogateId);

	PageResult<SysDictResp> pageList(DictListPageReq req);

	ApiResp<String> delete(Long surrogateId);

	ApiResp<Map<String, List<SysDictDetailResp>>> dictDetailMapping();
}