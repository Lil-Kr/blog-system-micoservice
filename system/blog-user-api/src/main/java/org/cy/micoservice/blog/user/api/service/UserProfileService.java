package org.cy.micoservice.blog.user.api.service;

import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.entity.user.model.provider.pojo.User;
import org.cy.micoservice.blog.user.api.vo.resp.SysUserResp;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
public interface UserProfileService {

  User profile(Long id);

  ApiResp<SysUserResp> getUserBySurrogateId(Long surrogateId);
}
