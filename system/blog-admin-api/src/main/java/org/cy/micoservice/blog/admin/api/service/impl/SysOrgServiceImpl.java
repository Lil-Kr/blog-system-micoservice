package org.cy.micoservice.blog.admin.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.admin.api.service.SysOrgService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysOrgFacade;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.dto.org.OrgLevelDto;
import org.cy.micoservice.blog.entity.admin.model.req.sys.org.OrgListAllReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.org.OrgPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.org.OrgReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.org.SysOrgResp;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/3
 * @Description: org service
 */
@Slf4j
@Service
public class SysOrgServiceImpl implements SysOrgService {

	@DubboReference(check = false)
	private SysOrgFacade orgFacade;

	@Override
	public ApiResp<String> add(OrgReq req) {
		return orgFacade.add(req);
	}

	@Override
	public ApiResp<String> edit(OrgReq req) {
		return orgFacade.edit(req);
	}

	@Override
	public List<OrgLevelDto> orgTree() {
		return orgFacade.orgTree();
	}

	@Override
	public ApiResp<String> delete(Long surrogateId) {
		return orgFacade.delete(surrogateId);
	}

	@Override
	public PageResult<SysOrgResp> pageList(OrgPageReq req) {
		return orgFacade.pageList(req);
	}

	@Override
	public List<SysOrgResp> list(OrgListAllReq req) {
		return orgFacade.list(req);
	}
}
