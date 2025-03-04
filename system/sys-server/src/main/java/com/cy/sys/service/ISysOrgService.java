package com.cy.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.common.utils.apiUtil.ApiResp;
import com.cy.sys.pojo.entity.SysOrg;
import com.cy.sys.pojo.param.org.OrgDeleteParam;
import com.cy.sys.pojo.param.org.OrgGetChildrenParam;
import com.cy.sys.pojo.param.org.OrgListAllParam;
import com.cy.sys.pojo.param.org.OrgParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lil-Kr
 * @since 2020-11-24
 */
public interface ISysOrgService extends IService<SysOrg> {

    ApiResp add(OrgParam param) throws Exception;

    ApiResp edit(OrgParam param) throws Exception;

    ApiResp getChildrenOrgList(OrgGetChildrenParam dto) throws Exception;

    ApiResp orgTree() throws Exception;

    ApiResp delete(OrgDeleteParam dto) throws Exception;

    ApiResp orgListAll(OrgListAllParam param) throws Exception;

    ApiResp orgListPage(OrgListAllParam param) throws Exception;

}
