package org.cy.micoservice.blog.common.exception;


import org.cy.micoservice.blog.common.enums.BaseEnum;
import org.cy.micoservice.blog.common.exception.interfacese.BaseError;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Lil-K
 * @Date: 2024/3/14
 * @Description: 业务异常处理
 */
public class BizException extends RuntimeException implements BaseError, Serializable {

  @Serial
  private static final long serialVersionUID = 4812984750725110363L;

  private BaseEnum baseEnum;

  private Integer errorCode;

  private String errorMsg;

  public BizException(String message){
    super(message);
  }

  public BizException(BaseError baseError) {
    super(baseError.getErrorMsg());
    this.errorCode = baseError.getErrorCode();
    this.errorMsg = baseError.getErrorMsg();
  }

  public BizException(BaseEnum baseEnum) {
    super(baseEnum.getMessage());
    this.baseEnum = baseEnum;
  }

  public BizException(Integer errorCode, String errorMsg) {
    super(errorMsg);
    this.errorCode = errorCode;
    this.errorMsg = errorMsg;
  }

  public BizException(String message, BaseEnum baseEnum) {
    super(message);
    this.baseEnum = baseEnum;
  }

  public BaseEnum getReturnCodeEnum() {
    return baseEnum;
  }

  @Override
  public Integer getErrorCode() {
    return this.baseEnum.getErrorCode();
  }

  @Override
  public String getErrorMsg() {
    return this.baseEnum.getErrorMsg();
  }
}
