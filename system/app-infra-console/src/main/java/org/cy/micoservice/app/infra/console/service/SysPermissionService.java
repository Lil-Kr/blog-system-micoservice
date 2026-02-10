package org.cy.micoservice.app.infra.console.service;


import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.entity.infra.console.model.req.sys.permission.PermissionReq;

import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/3/18
 * @Description: 菜单
 */
public interface SysPermissionService {

  // 获取当前用户的菜单数据和按钮数据
  ApiResp<Map<String, Object>> permission(PermissionReq req);
}
