package org.cy.micoservice.blog.admin.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysDictFacade;
import org.cy.micoservice.blog.admin.provider.service.SysDictService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictDetailReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictSaveReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictDetailResp;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Service
@DubboService
public class SysDictFacadeImpl implements SysDictFacade {

  @Autowired
  private SysDictService dictService;

  @Override
  public ApiResp<String> add(DictSaveReq req) {
    return dictService.add(req);
  }

  @Override
  public ApiResp<String> edit(DictSaveReq req) {
    return dictService.edit(req);
  }

  @Override
  public PageResult<SysDictResp> listAll() {
    return dictService.listAll();
  }

  @Override
  public ApiResp<SysDictResp> dictDetailList(DictDetailReq req) {
    return dictService.dictDetailList(req);
  }

  @Override
  public SysDictResp getDict(Long surrogateId) {
    return dictService.getDict(surrogateId);
  }

  @Override
  public PageResult<SysDictResp> pageList(DictListPageReq req) {
    return dictService.pageList(req);
  }

  @Override
  public ApiResp<String> delete(Long surrogateId) {
    return dictService.delete(surrogateId);
  }

  @Override
  public ApiResp<Map<String, List<SysDictDetailResp>>> dictDetailMapping() {
    return dictService.dictDetailMapping();
  }
}
