package org.cy.micoservice.blog.admin.provider.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysRoleAdmin;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @Author: Lil-K
 * @since 2020-11-24
 */
@Repository
public interface SysRoleAdminMapper extends BaseMapper<SysRoleAdmin> {

  List<Long> selectRoleIdListByAdminId(@Param("adminId") Long adminId);

  List<Long> selectAdminIdListByRoleId(@Param("roleId") Long roleId);
}
