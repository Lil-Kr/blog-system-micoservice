package org.cy.micoservice.blog.admin.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysOrgFacade;
import org.cy.micoservice.blog.admin.provider.service.SysOrgService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.dto.org.OrgLevelDto;
import org.cy.micoservice.blog.entity.admin.model.req.sys.org.OrgListAllReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.org.OrgPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.org.OrgReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.org.SysOrgResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Service
@DubboService
public class SysOrgFacadeImpl implements SysOrgFacade {

  @Autowired
  private SysOrgService orgService;

  @Override
  public ApiResp<String> add(OrgReq req) {
    return orgService.add(req);
  }

  @Override
  public ApiResp<String> edit(OrgReq req) {
    return orgService.edit(req);
  }

  @Override
  public List<OrgLevelDto> orgTree() {
    return orgService.orgTree();
  }

  @Override
  public ApiResp<String> delete(Long surrogateId) {
    return orgService.delete(surrogateId);
  }

  @Override
  public PageResult<SysOrgResp> pageList(OrgPageReq req) {
    return orgService.pageList(req);
  }

  @Override
  public List<SysOrgResp> list(OrgListAllReq req) {
    return orgService.list(req);
  }
}
