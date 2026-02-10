package org.cy.micoservice.app.framework.mybatis.starter.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.cy.micoservice.app.common.security.Crypto;
import org.cy.micoservice.app.framework.mybatis.starter.anno.Encrypt;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/7/20
 * @Description: 对update操作, 底层兼容save, saveBatch, update
 */
@Slf4j
@Intercepts({
  @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
public class EncryptInterceptor implements Interceptor {

  private static final String METHOD = "update";

  private Crypto crypto;

  public EncryptInterceptor(Crypto crypto) {
    this.crypto = crypto;
  }

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    if (!StringUtils.equals(METHOD, invocation.getMethod().getName())) {
      return invocation.proceed();
    }

    // 根据update拦截规则, 第0个参数一定是MappedStatement, 第1个参数是需要进行判断的参数
    Object param = invocation.getArgs()[1];
    if (Objects.isNull(param)) {
      return invocation.proceed();
    }

    List<Field> encryptFields = FieldUtils.getFieldsListWithAnnotation(param.getClass(), Encrypt.class);
    // 无字段需要加密, 则跳过
    if (CollectionUtils.isEmpty(encryptFields)) {
      return invocation.proceed();
    }
    for (Field field : encryptFields) {
      if (!field.getType().equals(String.class)) {
        continue;
      }
      field.setAccessible(true);
      String originValue = (String) field.get(param);
      if (originValue == null || originValue.isBlank()) {
        continue;
      }
      //设置加密后的字符串
      String encryptResult = crypto.encrypt(originValue);
      field.set(param, encryptResult);
    }
    return invocation.proceed();
  }
}
