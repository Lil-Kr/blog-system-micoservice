package org.cy.micoservice.app.user.api.service;

import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.entity.user.model.provider.pojo.User;
import org.cy.micoservice.app.user.api.vo.resp.SysUserResp;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
public interface UserProfileService {

  User profile(Long id);

  ApiResp<SysUserResp> getUserBySurrogateId(Long surrogateId);
}
