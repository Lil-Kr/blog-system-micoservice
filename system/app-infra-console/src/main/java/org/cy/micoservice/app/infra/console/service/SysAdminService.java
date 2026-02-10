package org.cy.micoservice.app.infra.console.service;

import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.entity.infra.console.model.entity.sys.SysAdmin;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.admin.*;
import org.cy.micoservice.app.entity.infra.console.model.resp.sys.admin.SysAdminResp;

/**
 * @Author: Lil-K
 * @Date: 2025/3/7
 * @Description:
 */
public interface SysAdminService {

	SysAdmin getUserById(Long id);

	SysAdminResp getUserBySurrogateId(Long surrogateId);

	ApiResp<SysAdmin> adminLogin(AdminLoginReq reqParam) throws Exception;

	ApiResp<Integer> registerAdmin(AdminRegisterReq req);

	ApiResp<String> add(AdminSaveReq req);

	PageResult<SysAdminResp> pageList(AdminListPageReq req);

	ApiResp<String> edit(AdminSaveReq req);

	ApiResp<String> delete(Long surrogateId);

  ApiResp<String> uploadAvatar(AvatarUploadReq req) throws Exception;

  /**
   * 获取token
   * @return
   */
  ApiResp<SysAdmin> getToken() throws Exception;
}
