package org.cy.micoservice.blog.user.provider.facade;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.cy.micoservice.blog.common.base.provider.RpcResponse;
import org.cy.micoservice.blog.common.utils.BeanCopyUtils;
import org.cy.micoservice.blog.entity.user.model.provider.po.User;
import org.cy.micoservice.blog.entity.user.model.provider.resp.UserResp;
import org.cy.micoservice.blog.user.facade.dto.req.TestReq;
import org.cy.micoservice.blog.user.facade.dto.resp.SysUserDTO;
import org.cy.micoservice.blog.user.facade.dto.resp.UserRespDTO;
import org.cy.micoservice.blog.user.facade.interfaces.UserFacade;
import org.cy.micoservice.blog.user.provider.service.UserService;
import org.cy.micoservice.blog.user.provider.service.UserShardService;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/11/20
 * @Description: 用户信息rpc
 */
@Slf4j
@DubboService
public class UserFacadeImpl implements UserFacade {

  @Resource
  private UserService userService;
  @Resource
  private UserShardService userShardService;

  @Override
  public User queryUserById(Long userId) {
    return userService.getUserById(userId);
  }

  @Override
  public SysUserDTO getUserBySurrogateId(Long surrogateId) {
    UserResp user = userService.getUserBySurrogateId(surrogateId);
    SysUserDTO dto = new SysUserDTO();
    BeanUtils.copyProperties(user, dto);
    return dto;
  }

  @Override
  public String test(TestReq req) {
    log.info("参数调用: {}", JSONObject.toJSONString(req));
    return JSONObject.toJSONString(req);
  }

  @Override
  public RpcResponse<List<UserRespDTO>> queryInUserIds(List<Long> userIds) {
    List<UserRespDTO> userRespDTOList = BeanCopyUtils.convertList(userShardService.queryInUserIds(userIds), UserRespDTO.class);
    return RpcResponse.success(userRespDTOList);
  }
}
