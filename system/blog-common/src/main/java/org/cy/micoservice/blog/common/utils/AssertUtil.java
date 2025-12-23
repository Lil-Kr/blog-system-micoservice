package org.cy.micoservice.blog.common.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.common.enums.response.RpcReturnCodeEnum;
import org.cy.micoservice.blog.common.exception.interfacese.BaseError;
import org.cy.micoservice.blog.common.exception.BizException;
import org.cy.micoservice.blog.common.exception.RpcException;

import java.util.Collection;
import java.util.Objects;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
public class AssertUtil {

  /**
   * 判断参数不能为空
   * @param obj
   */
  public static void isNotNull(Object obj, BaseError baseError) {
    if (obj == null) {
      throw new RpcException(baseError);
    }
  }

  /**
   * 判断参数必须为空
   * @param obj
   */
  public static void isNull(Object obj) {
    if (obj != null) {
      throw new RpcException(RpcReturnCodeEnum.RPC_PARAMETER_ERROR);
    }
  }

  /**
   * 判断字符串不能为空
   * @param obj
   */
  public static void isNotBlank(Object obj, BaseError baseError) {
    if (Objects.isNull(obj)) {
      throw new RpcException(baseError);
    }
  }

  /**
   * 判断字符串不能为空
   *
   * @param strs
   * @param
   */
  public static void isAllNotBlank(BaseError baseError, String ... strs) {
    for (String str : strs) {
      isNotBlank(str, baseError);
    }
  }

  /**
   * 判断所有参数不能为空
   *
   * @param objs
   */
  public static void isAllNotNull(BaseError baseError, Object... objs) {
    for (Object obj : objs) {
      isNotNull(obj, baseError);
    }
  }

  /**
   * 集合为空 则抛出异常
   * @param collection
   */
  public static void isNotEmpty(Collection collection) {
    if (CollectionUtils.isEmpty(collection)) {
      throw new RpcException(RpcReturnCodeEnum.RPC_PARAMETER_ERROR);
    }
  }

  /**
   * 集合为空 则抛出异常
   * @param collections
   */
  public static void isAllNotEmpty(Collection ... collections) {
    for (Collection col : collections) {
      isNotEmpty(col);
    }
  }

  /**
   * flag == true
   * @param flag
   */
  public static void isTrue(boolean flag, BaseError baseError) {
    if (!flag) {
      throw new BizException(baseError);
    }
  }

  /**
   * flag == false
   * @param flag
   */
  public static void isFalse(boolean flag) {
    if (flag) {
      throw new RpcException(RpcReturnCodeEnum.RPC_REQUEST_ERROR);
    }
  }
}