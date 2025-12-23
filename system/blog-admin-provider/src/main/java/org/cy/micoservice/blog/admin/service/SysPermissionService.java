package org.cy.micoservice.blog.admin.service;

import org.cy.micoservice.blog.common.base.ApiResp;

import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/3/18
 * @Description: 菜单
 */
public interface SysPermissionService {

  // 获取当前用户的菜单数据和按钮数据
  ApiResp<Map<String, Object>> permission();
}
