package org.cy.micoservice.blog.framework.web.starter.web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.constants.gateway.GatewayHeadersConstants;
import org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.blog.common.exception.BizException;
import org.cy.micoservice.blog.common.security.impl.AES128GCMCrypto;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.framework.web.starter.enums.RequestEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: 2025/11/23
 * @Description: 认证拦截器
 */
@Slf4j
@Configuration
public class AuthInterceptor implements HandlerInterceptor, InitializingBean {

//  @DubboReference(check = false)
//  private IAuthFacade authFacade;

  @Value("${blog.gateway.decrypty.secret_key:PxMNarWuqoNFFGJ5QGgesg==}")
  private String decryptSecretKey;

  private AES128GCMCrypto aes128GCMCrypto;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (!(handler instanceof HandlerMethod)) {
      log.info("invalid request,uri:{}", request.getRequestURI());
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();
    log.info("uri: {}", request.getRequestURI());
    NoAuthCheck noAuthCheck = method.getAnnotation(NoAuthCheck.class);
    if (Objects.nonNull(noAuthCheck)) {
      return true;
    }

    String identifyHeader = request.getHeader(GatewayHeadersConstants.X_GATEWAY_IDENTIFY);
    if (StringUtils.isBlank(identifyHeader)) {
      throw new BizException(ApiReturnCodeEnum.NO_ACCESS);
    }

    try {
      // todo: 测试用, 后续需要删掉
      String decryptBody = aes128GCMCrypto.decrypt(identifyHeader);
      JSONObject jsonObject = JSON.parseObject(decryptBody);
      RequestContext.set(RequestEnum.USER_ID, jsonObject.getLongValue("userId"));
      // RequestContext.set(RequestEnum.USER_ID, 109891L);
      // RequestContext.set(RequestEnum.USER_ID, 708913L);
    } catch (Exception e) {
      log.info("login error msg: ", e);
      throw new BizException(ApiReturnCodeEnum.NO_ACCESS);
    }
    return true;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.aes128GCMCrypto = new AES128GCMCrypto(decryptSecretKey);
  }
}