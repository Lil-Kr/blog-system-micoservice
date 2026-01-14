package org.cy.micoservice.blog.infra.console.dao.rbac;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysOrg;
import org.cy.micoservice.blog.entity.admin.model.req.sys.org.OrgListAllReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.org.OrgPageReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.org.SysOrgResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface SysOrgMapper extends BaseMapper<SysOrg> {

  List<SysOrg> selectChildOrgList(@Param("level") String oldLevelPrefix);

  List<SysOrg> selectChildOrgListByParentId(@Param("parentId") Long parentId);

  List<SysOrgResp> pageList(@Param("param") OrgPageReq req);

  Integer countByList(@Param("param") OrgPageReq req);

  List<SysOrgResp> retrieveAllList(@Param("param") OrgListAllReq req);
}
