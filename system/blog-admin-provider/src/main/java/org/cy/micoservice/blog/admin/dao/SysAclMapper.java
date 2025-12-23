package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.SysAcl;
import org.cy.micoservice.blog.entity.admin.model.req.acl.AclPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.acl.AclReq;
import org.cy.micoservice.blog.entity.admin.model.resp.acl.SysAclResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/9
 * @Description:
 */
@Repository
public interface SysAclMapper extends BaseMapper<SysAcl> {

  List<SysAcl> selectAclListByAclIdList(@Param("userAclIdList") List<Long> userAclIdList);

  List<SysAclResp> pageAclList(@Param("param") AclPageReq req);

  Integer countPageAclList(@Param("param") AclPageReq req);

  List<Long> selectAclIdAllList(@Param("param") AclReq req);

  SysAcl getAcl(@Param("param") AclReq req);

  List<SysAcl> selectAclListByUrl(@Param("url") String url);
}
