package org.cy.micoservice.app.infra.console.service;

import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.infra.facade.dto.org.OrgLevelDto;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.org.OrgListAllReq;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.org.OrgPageReq;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.org.OrgReq;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.org.SysOrgResp;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/3
 * @Description:
 */
public interface SysOrgService {
	
	ApiResp<String> add(OrgReq req);

	ApiResp<String> edit(OrgReq req);

	List<OrgLevelDto> orgTree();

	ApiResp<String> delete(Long surrogateId);

	PageResult<SysOrgResp> pageList(OrgPageReq req);

	List<SysOrgResp> list(OrgListAllReq req);

}