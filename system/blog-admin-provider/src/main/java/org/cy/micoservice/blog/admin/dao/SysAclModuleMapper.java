package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.SysAclModule;
import org.cy.micoservice.blog.entity.admin.model.req.aclmodule.AclModuleListReq;
import org.cy.micoservice.blog.entity.admin.model.resp.aclmodule.SysAclModuleResp;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author: Lil-K
 * @since 2020-11-24
 */
@Repository
public interface SysAclModuleMapper extends BaseMapper<SysAclModule> {

  List<SysAclModule> selectChildAclModuleListByParentId(@Param("parentId") Long surrogateId);

  List<SysAclModuleResp> selectAclModuleList(@Param("param") AclModuleListReq req);

  List<SysAclModule> selectAclModuleListByIds(@Param("aclModuleIds") Set<Long> aclModuleIds);
}
