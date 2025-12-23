package org.cy.micoservice.blog.user.facade.interfaces;

import org.cy.micoservice.blog.user.facade.dto.req.TestReq;
import org.cy.micoservice.blog.user.facade.dto.resp.SysUserDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
public interface UserFacade {

  String queryUserById(Long userId);

  SysUserDTO getUserBySurrogateId(Long surrogateId);

  String test(TestReq req);
}
