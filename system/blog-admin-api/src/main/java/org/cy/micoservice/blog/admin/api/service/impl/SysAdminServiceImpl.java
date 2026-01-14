package org.cy.micoservice.blog.admin.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.admin.api.service.SysAdminService;
import org.cy.micoservice.blog.admin.facade.dto.sys.admin.AdminLoginAdminReqDTO;
import org.cy.micoservice.blog.admin.facade.interfaces.SysAdminFacade;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.base.api.PageResult;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.entity.admin.model.entity.sys.SysAdmin;
import org.cy.micoservice.blog.entity.admin.model.req.sys.admin.*;
import org.cy.micoservice.blog.entity.admin.model.resp.sys.admin.SysAdminResp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/3/6
 * @Description:
 */
@Slf4j
@Service
public class SysAdminServiceImpl implements SysAdminService {

	@Value("${upload.rootDir}")
	private String rootDir;

	@Value("${upload.uploadDir}")
	private String uploadDir;

	@Value("${upload.adminUserPath}")
	private String adminUserPath;

	@DubboReference(check = false)
	private SysAdminFacade adminFacade;

	@Override
	public ApiResp<SysAdmin> adminLogin(AdminLoginAdminReq req) {
		AdminLoginAdminReqDTO adminLoginAdminReqDTO = BeanCopyUtils.convert(req, AdminLoginAdminReqDTO.class);
		return adminFacade.adminLogin(adminLoginAdminReqDTO);
	}

	@Override
	public ApiResp<Integer> registerAdmin(AdminRegisterReq req) {
		return adminFacade.registerAdmin(req);
	}

	@Override
	public SysAdmin getAdminById(Long id) {
		return adminFacade.getAdminById(id);
	}

	@Override
	public ApiResp<String> add(AdminSaveReq req) {
		return adminFacade.add(req);
	}

	@Override
	public PageResult<SysAdminResp> pageList(AdminListPageReq req) {
		return adminFacade.pageList(req);
	}

	@Override
	public ApiResp<String> edit(AdminSaveReq req) {
		return adminFacade.edit(req);
	}

	@Override
	public ApiResp<String> delete(Long id) {
		return adminFacade.delete(id);
	}

	@Override
	public ApiResp<String> uploadAvatar(AvatarUploadReq req) throws Exception {
		return adminFacade.uploadAvatar(req);
	}
}