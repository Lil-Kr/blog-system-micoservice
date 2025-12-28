package org.cy.micoservice.blog.user.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.user.api.service.UserProfileService;
import org.cy.micoservice.blog.user.api.vo.resp.SysUserResp;
import org.cy.micoservice.blog.user.facade.dto.resp.SysUserDTO;
import org.cy.micoservice.blog.user.facade.interfaces.UserFacade;
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
  public String profile(Long id) {
    return userFacade.queryUserById(id);
  }

  @Override
  public ApiResp<SysUserResp> getUserBySurrogateId(Long surrogateId) {
    SysUserDTO user = userFacade.getUserBySurrogateId(surrogateId);
    SysUserResp res = new SysUserResp();
    BeanUtils.copyProperties(user, res);
    return ApiResp.success(res);
  }
}
