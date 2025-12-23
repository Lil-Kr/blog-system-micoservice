package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.SysRoleUser;
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
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

  List<Long> selectRoleIdListByUserId(@Param("userId") Long userId);

  List<Long> selectUserIdListByRoleId(@Param("roleId") Long roleId);
}
