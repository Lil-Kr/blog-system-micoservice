package org.cy.micoservice.blog.admin.provider.facade;

import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysPermissionFacade;
import org.cy.micoservice.blog.admin.provider.service.SysPermissionService;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.req.sys.permission.PermissionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2026/1/14
 * @Description:
 */
@Service
@DubboService
public class SysPermissionFacadeImpl implements SysPermissionFacade {

  @Autowired
  private SysPermissionService permissionService;

  @Override
  public ApiResp<Map<String, Object>> permission(PermissionReq req) {
    return permissionService.permission(req);
  }
}
