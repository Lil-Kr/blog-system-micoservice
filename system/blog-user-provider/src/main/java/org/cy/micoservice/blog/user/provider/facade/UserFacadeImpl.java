package org.cy.micoservice.blog.user.provider.facade;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.user.facade.dto.req.TestReq;
import org.cy.micoservice.blog.user.facade.dto.resp.SysUserDTO;
import org.cy.micoservice.blog.user.facade.interfaces.UserFacade;
import org.cy.micoservice.blog.user.provider.pojo.resp.SysUserResp;
import org.cy.micoservice.blog.user.provider.service.UserService;
import org.springframework.beans.BeanUtils;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description:
 */
@Slf4j
@DubboService
public class UserFacadeImpl implements UserFacade {

  @Resource
  private UserService userService;

  @Override
  public String queryUserById(Long userId) {
    return userService.queryUserById(120000l);
  }

  @Override
  public SysUserDTO getUserBySurrogateId(Long surrogateId) {
    SysUserResp user = userService.getUserBySurrogateId(surrogateId);
    SysUserDTO dto = new SysUserDTO();
    BeanUtils.copyProperties(user, dto);
    return dto;
  }

  @Override
  public String test(TestReq req) {
    log.info("参数调用: {}", JSONObject.toJSONString(req));
    return JSONObject.toJSONString(req);
  }
}
