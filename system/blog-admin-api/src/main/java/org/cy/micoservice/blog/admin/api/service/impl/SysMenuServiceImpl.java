package org.cy.micoservice.blog.admin.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.admin.api.service.SysPermissionService;
import org.cy.micoservice.blog.admin.facade.interfaces.SysPermissionFacade;
import org.cy.micoservice.blog.admin.facade.interfaces.SysTreeFacade;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.admin.model.req.sys.permission.PermissionReq;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/3/18
 * @Description:
 */
@Service
public class SysMenuServiceImpl implements SysPermissionService {

  @DubboReference(check = false)
  private SysTreeFacade treeFacade;

  @DubboReference(check = false)
  private SysPermissionFacade permissionFacade;

  /**
   * 获取用户-菜单
   * 菜单配置:
   *  1. 如果当前权限模块就是需要跳转的url, 那么就配置权限点, 并且设置为[菜单]模式
   *  2. 如果还有下一层菜单, 当前模块就不配置权限点, 并且配置为 [菜单] 模式
   *  3. 最后一层不需要配置为[菜单]模式, 填写正确的url即可
   * @return
   */
  @Override
  public ApiResp<Map<String, Object>> permission(PermissionReq req) {
    return permissionFacade.permission(req);
  }
}
