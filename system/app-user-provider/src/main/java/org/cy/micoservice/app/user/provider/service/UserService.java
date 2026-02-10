package org.cy.micoservice.app.user.provider.service;

import org.cy.micoservice.app.common.base.api.ApiResp;
import org.cy.micoservice.app.common.base.api.PageResult;
import org.cy.micoservice.app.entity.user.model.provider.pojo.User;
import org.cy.micoservice.app.entity.user.model.provider.req.UserListPageReq;
import org.cy.micoservice.app.entity.user.model.provider.req.UserLoginAdminReq;
import org.cy.micoservice.app.entity.user.model.provider.req.UserRegisterReq;
import org.cy.micoservice.app.entity.user.model.provider.req.UserSaveReq;
import org.cy.micoservice.app.entity.user.model.provider.resp.UserResp;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
public interface UserService {

  String queryUserById(Long userId);

  User getUserById(Long id);

  UserResp getUserBySurrogateId(Long surrogateId);

  ApiResp<User> adminLogin(UserLoginAdminReq req);

  ApiResp<Integer> registerAdmin(UserRegisterReq req);

  ApiResp<String> add(UserSaveReq req);

  PageResult<UserResp> pageList(UserListPageReq req);

  ApiResp<String> edit(UserSaveReq req);

  ApiResp<String> delete(Long surrogateId);

}
