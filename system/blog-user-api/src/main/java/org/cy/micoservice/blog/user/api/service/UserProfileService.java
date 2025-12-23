package org.cy.micoservice.blog.user.api.service;

import org.cy.micoservice.blog.common.base.ApiResp;
import org.cy.micoservice.blog.user.api.vo.resp.SysUserResp;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
public interface UserProfileService {

  String profile(Long id);

  ApiResp<SysUserResp> getUserBySurrogateId(Long surrogateId);
}
