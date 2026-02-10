package org.cy.micoservice.app.user.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.entity.user.model.provider.pojo.User;
import org.cy.micoservice.app.user.api.service.UserProfileService;
import org.cy.micoservice.app.user.api.vo.resp.SysUserResp;
import org.cy.micoservice.app.user.facade.dto.resp.SysUserDTO;
import org.cy.micoservice.app.user.facade.interfaces.UserFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

  @DubboReference(check = false)
  private UserFacade userFacade;

  @Override
  public User profile(Long userId) {
    return userFacade.queryUserById(userId);
  }

  @Override
  public ApiResp<SysUserResp> getUserBySurrogateId(Long surrogateId) {
    SysUserDTO user = userFacade.getUserBySurrogateId(surrogateId);
    SysUserResp res = new SysUserResp();
    BeanUtils.copyProperties(user, res);
    return ApiResp.success(res);
  }
}
