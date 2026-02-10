package org.cy.micoservice.blog.infra.console.dao.rbac;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.infra.console.model.entity.sys.SysRole;
import org.cy.micoservice.blog.entity.infra.console.model.req.sys.role.RoleListPageReq;
import org.cy.micoservice.blog.entity.infra.console.model.resp.sys.role.SysRoleResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/11
 * @Description:
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

  Integer deleteBySurrogateId(@Param("surrogateId") Long surrogateId);

  List<SysRoleResp> pageRoleList(@Param("param") RoleListPageReq req);

  Integer countRolePage(@Param("param") RoleListPageReq req);

  List<SysRoleResp> selectRoleLIstByIds(@Param("roleIdList") List<Long> roleIdList);
}