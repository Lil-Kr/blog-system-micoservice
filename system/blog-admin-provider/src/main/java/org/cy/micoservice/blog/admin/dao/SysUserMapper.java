package org.cy.micoservice.blog.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.entity.admin.model.entity.SysUser;
import org.cy.micoservice.blog.entity.admin.model.req.user.AvatarUploadReq;
import org.cy.micoservice.blog.entity.admin.model.req.user.UserListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.user.UserLoginAdminReq;
import org.cy.micoservice.blog.entity.admin.model.req.user.UserSaveReq;
import org.cy.micoservice.blog.entity.admin.model.resp.admin.SysUserResp;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/7
 * @Description:
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

  SysUser loginAdmin(@Param("param") UserLoginAdminReq req);

  SysUser getUserById(Long id);

  SysUserResp getUserBySurrogateId(Long surrogateId);

  SysUser getUserByToken(String token);

  SysUser getUserByKeyword(@Param("param") UserLoginAdminReq req);

  SysUser getUserByAccount(String account);

  Integer updateUserBySurrogateId(@Param("param") SysUser user);

  List<SysUserResp> pageUserList(@Param("param") UserListPageReq req);

  Integer countUserList(@Param("param") UserListPageReq req);

  List<SysUserResp> selectUserInfoExist(@Param("param") UserSaveReq req);

  List<SysUserResp> selectUserListByIds(@Param("userIdList") List<Long> userIdList);

  List<SysUserResp> selectUserList();

  List<SysUser> selectUserAllList();

  int updateAvatar(@Param("param") AvatarUploadReq req);
}
