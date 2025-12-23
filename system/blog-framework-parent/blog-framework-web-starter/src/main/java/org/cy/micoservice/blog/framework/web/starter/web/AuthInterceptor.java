package org.cy.micoservice.blog.framework.web.starter.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum;
import org.cy.micoservice.blog.common.exception.BizException;
import org.cy.micoservice.blog.common.security.impl.AES128GCMCrypto;
import org.cy.micoservice.blog.common.utils.JsonUtil;
import org.cy.micoservice.blog.framework.web.starter.annotations.NoAuthCheck;
import org.cy.micoservice.blog.framework.web.starter.enums.RequestEnum;
import org.cy.micoservice.blog.entity.gateway.model.enums.GatewayHeadersConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: 2025/11/23
 * @Description: 认证拦截器
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor, InitializingBean {

//  @DubboReference(check = false)
//  private IAuthFacade authFacade;

  private AES128GCMCrypto aes128GCMCrypto;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (!(handler instanceof HandlerMethod)) {
      log.info("invalid request,uri:{}",request.getRequestURI());
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();
    log.info("uri:{}", request.getRequestURI());
    NoAuthCheck noAuthCheck = method.getAnnotation(NoAuthCheck.class);
    if (Objects.nonNull(noAuthCheck)) {
      return true;
    }

    String identifyHeader = request.getHeader(GatewayHeadersConstants.X_GATEWAY_IDENTIFY);
    if (identifyHeader == null) {
      throw new BizException(ApiReturnCodeEnum.NO_ACCESS);
    }

    try {
      String decryptBody = aes128GCMCrypto.decrypt(identifyHeader);
      Map<String, Object> token = JsonUtil.objectToMap(decryptBody);
      RequestContext.set(RequestEnum.USER_ID, token.get("userId"));
    } catch (Exception e) {
      throw new BizException(ApiReturnCodeEnum.NO_ACCESS);
    }
    return true;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.aes128GCMCrypto = new AES128GCMCrypto("PxMNarWuqoNFFGJ5QGgesg==");
  }
}