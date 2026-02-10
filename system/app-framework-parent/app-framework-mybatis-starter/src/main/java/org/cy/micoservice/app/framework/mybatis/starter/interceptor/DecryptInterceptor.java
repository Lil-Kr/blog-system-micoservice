package org.cy.micoservice.app.framework.mybatis.starter.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.cy.micoservice.app.common.security.Crypto;
import org.cy.micoservice.app.framework.mybatis.starter.anno.Decrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/7/20
 * @Description: 对select相关操作进行拦截
 */
@Slf4j
@Intercepts({
  @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class)
})
@Component
public class DecryptInterceptor implements Interceptor {

  private Crypto crypto;

  public DecryptInterceptor(Crypto crypto) {
    this.crypto = crypto;
  }

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    Object result = invocation.proceed();
    // 无论是返回单个对象还是集合, result都是ArrayList类型
    if (ClassUtils.isAssignable(Collection.class, result.getClass())) {
      Collection collectionResult = (Collection) result;
      for (Object resultObj : collectionResult) {
        tryDecryptValue(resultObj);
      }
    }
    return result;
  }

  /**
   * 尝试解密
   *
   * @param resultObj
   */
  private void tryDecryptValue(Object resultObj) {
    try {
      List<Field> decryptFields = FieldUtils.getFieldsListWithAnnotation(resultObj.getClass(), Decrypt.class);
      // 无字段需解密, 则跳过
      if (CollectionUtils.isEmpty(decryptFields)) {
        return;
      }
      for (Field field : decryptFields) {
        if (!field.getType().equals(String.class)) {
          continue;
        }
        field.setAccessible(true);
        String originValue = (String) field.get(resultObj);
        if (originValue == null || originValue.isBlank()) {
          continue;
        }
        field.set(resultObj, crypto.decrypt(originValue));
      }
    } catch (Exception e) {
      log.error("decrypt error", e);
    }
  }
}
