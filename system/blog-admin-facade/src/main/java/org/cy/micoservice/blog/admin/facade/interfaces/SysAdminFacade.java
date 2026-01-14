package org.cy.micoservice.blog.admin.facade.interfaces;

import org.cy.micoservice.blog.admin.facade.dto.sys.admin.AdminLoginAdminReqDTO;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.*;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.admin.SysAdminResp;

/**
 * @Author: Lil-K
 * @Date: 2025/3/7
 * @Description:
 */
public interface SysAdminFacade {

	ApiResp<SysAdmin> adminLogin(AdminLoginAdminReqDTO reqDTO);

	ApiResp<Integer> registerAdmin(AdminRegisterReq req);

	SysAdmin getAdminById(Long id);

	ApiResp<String> add(AdminSaveReq req);

	PageResult<SysAdminResp> pageList(AdminListPageReq req);

	ApiResp<String> edit(AdminSaveReq req);

	ApiResp<String> delete(Long id);

  ApiResp<String> uploadAvatar(AvatarUploadReq req) throws Exception;
}
