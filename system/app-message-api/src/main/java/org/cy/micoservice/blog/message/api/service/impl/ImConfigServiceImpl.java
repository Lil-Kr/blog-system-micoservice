package org.cy.micoservice.blog.message.api.service.impl;

import org.cy.micoservice.blog.entity.message.model.provider.resp.ImConfigResp;
import org.cy.micoservice.blog.framework.identiy.starter.config.AuthProperties;
import org.cy.micoservice.blog.framework.identiy.starter.uitls.JWTUtil;
import org.cy.micoservice.blog.message.api.config.ApplicationProperties;
import org.cy.micoservice.blog.message.api.service.ImConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: im配置service
 */
@Service
public class ImConfigServiceImpl implements ImConfigService {

  @Autowired
  private ApplicationProperties applicationProperties;
  @Autowired
  private AuthProperties authProperties;

  /**
   * 获取im的服务器配置信息地址
   * @param userId
   * @return
   */
  @Override
  public ImConfigResp getImChatConfig(Long userId) {
    // im token的有效期-7天
    String loginToken = JWTUtil.generateToken(
      String.valueOf(userId),
      new HashMap<>(),
      authProperties.getSecretKey(),
      TimeUnit.DAYS.toMillis(7)
    );

    return ImConfigResp
      .builder()
      .imServerAddress(applicationProperties.getImServerAddress())
      .imToken(loginToken)
      .build();
  }
}
