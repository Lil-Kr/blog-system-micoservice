package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.common.base.PageResult;
import org.cy.micoservice.blog.entity.admin.model.dto.org.OrgLevelDto;
import org.cy.micoservice.blog.entity.admin.model.req.org.OrgListAllReq;
import org.cy.micoservice.blog.entity.admin.model.req.org.OrgPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.org.OrgReq;
import org.cy.micoservice.blog.entity.admin.model.resp.org.SysOrgResp;

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