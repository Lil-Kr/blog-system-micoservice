package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.SysRole;
import org.cy.micoservice.blog.entity.admin.model.req.role.RoleListPageReq;
import org.cy.micoservice.blog.entity.admin.model.resp.role.SysRoleResp;
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