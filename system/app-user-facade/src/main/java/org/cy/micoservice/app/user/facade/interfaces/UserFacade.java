package org.cy.micoservice.app.user.facade.interfaces;

import org.cy.micoservice.app.common.base.provider.RpcResponse;
import org.cy.micoservice.app.entity.user.model.provider.pojo.User;
import org.cy.micoservice.app.user.facade.dto.resp.SysUserDTO;
import org.cy.micoservice.app.user.facade.dto.resp.UserRespDTO;
import org.cy.micoservice.app.user.facade.dto.req.TestReq;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description: 用户服务facade
 */
public interface UserFacade {

  User queryUserById(Long userId);

  SysUserDTO getUserBySurrogateId(Long surrogateId);

  String test(TestReq req);

  /**
   * 批量查询用户信息
   * @param userIds
   * @return
   */
  RpcResponse<List<UserRespDTO>> queryInUserIds(List<Long> userIds);
}
