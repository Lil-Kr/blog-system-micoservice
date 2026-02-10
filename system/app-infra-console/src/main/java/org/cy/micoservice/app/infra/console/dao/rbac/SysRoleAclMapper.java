package org.cy.micoservice.app.infra.console.dao.rbac;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysRoleAcl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/11
 * @Description:
 */
@Repository
public interface SysRoleAclMapper extends BaseMapper<SysRoleAcl> {

  List<Long> selectAclIdListByRoleIdList(@Param("userRoleIdList") List<Long> userRoleIdList);

  List<Long> selectAclIdListByRoleId(@Param("roleSurrogateId") Long roleSurrogateId);

}

