package org.cy.micoservice.blog.user.facade.interfaces;

import org.cy.micoservice.blog.common.base.provider.RpcResponse;
import org.cy.micoservice.blog.entity.user.model.provider.pojo.User;
import org.cy.micoservice.blog.user.facade.dto.req.TestReq;
import org.cy.micoservice.blog.user.facade.dto.resp.SysUserDTO;
import org.cy.micoservice.blog.user.facade.dto.resp.UserRespDTO;

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
