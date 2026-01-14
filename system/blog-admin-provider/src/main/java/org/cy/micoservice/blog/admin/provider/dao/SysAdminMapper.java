package org.cy.micoservice.blog.admin.provider.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cy.micoservice.blog.admin.facade.dto.sys.admin.AdminLoginAdminReqDTO;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AdminListPageReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AdminLoginAdminReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AdminSaveReq;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.AvatarUploadReq;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.admin.SysAdminResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/7
 * @Description:
 */
@Repository
public interface SysAdminMapper extends BaseMapper<SysAdmin> {

  SysAdmin loginAdmin(@Param("param") AdminLoginAdminReqDTO req);

  SysAdmin getAdminById(Long id);

  SysAdminResp getAdminBySurrogateId(Long surrogateId);

  SysAdmin getAdminByToken(String token);

  SysAdmin getAdminByKeyword(@Param("param") AdminLoginAdminReq req);

  SysAdmin getAdminByAccount(String account);

  Integer updateAdminById(@Param("param") SysAdmin user);

  List<SysAdminResp> pageAdminList(@Param("param") AdminListPageReq req);

  Integer countAdminList(@Param("param") AdminListPageReq req);

  List<SysAdminResp> selectAdminInfoExist(@Param("param") AdminSaveReq req);

  List<SysAdminResp> selectAdminListByIds(@Param("adminIdList") List<Long> adminIdList);

  List<SysAdminResp> selectAdminList();

  List<SysAdmin> selectAdminAllList();

  int updateAvatar(@Param("param") AvatarUploadReq req);
}
