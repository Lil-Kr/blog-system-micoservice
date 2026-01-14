package org.cy.micoservice.blog.admin.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysDictDetailFacade;
import org.cy.micoservice.blog.admin.provider.service.SysDictDetailService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysDictDetail;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.DictDetailPageListReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.dict.SaveDictDetailReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.dic.SysDictDetailResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Service
@DubboService
public class SysDictDetailFacadeImpl implements SysDictDetailFacade {

  @Autowired
  private SysDictDetailService dictDetailService;

  @Override
  public ApiResp<String> addDetail(SaveDictDetailReq req) {
    return dictDetailService.addDetail(req);
  }

  @Override
  public ApiResp<String> editDetail(SaveDictDetailReq req) {
    return dictDetailService.editDetail(req);
  }

  @Override
  public ApiResp<String> deleteDetail(Long surrogateId) {
    return dictDetailService.deleteDetail(surrogateId);
  }

  @Override
  public PageResult<SysDictDetailResp> pageDictDetailList(DictDetailPageListReq req) {
    return dictDetailService.pageDictDetailList(req);
  }

  @Override
  public SysDictDetail get(SaveDictDetailReq req) {
    return dictDetailService.get(req);
  }
}
