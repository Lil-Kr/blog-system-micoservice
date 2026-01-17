package org.cy.micoservice.blog.framework.web.starter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.cy.micoservice.blog.common.base.api.ApiResp;
import org.cy.micoservice.blog.common.exception.BizException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/3/11
 * @Description: 全局异常处理
 */
@RestControllerAdvice
@Configuration
@Slf4j
public class GlobalExceptionHandlerConfig {

  /**
   * catch BusinessException exception
   * @param exception
   * @return
   */
  @ExceptionHandler(BindException.class)
  public ApiResp<?> handleBusinessException(BindException exception) {
    BindingResult bindingResult = exception.getBindingResult();
    List<String> errorMsgList = new ArrayList<>();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    for (FieldError fieldError : fieldErrors) {
      errorMsgList.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
    }
    return ApiResp.warning(errorMsgList.toString());
  }

  /**
   * com.fasterxml.jackson.databind.exc.InvalidFormatException
   * @param request
   * @param exception
   * @return
   */
  @ExceptionHandler(value = MissingServletRequestParameterException.class)
  @ResponseBody
  public ApiResp<String> validateParameterException(HttpServletRequest request,
                                                    MissingServletRequestParameterException exception) {
    String message = exception.getMessage();
    return ApiResp.warning(message);
  }

  /**
   * 校验参数类型不一致
   * @param request
   * @param exception
   * @return
   */
  @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
  public ApiResp<String> validateNumberFormatException(HttpServletRequest request,
                                                       MethodArgumentTypeMismatchException exception) {
    String message = exception.getMessage();
    return ApiResp.warning(message);
  }

  /**
   * 参数校验异常捕获
   * @param request
   * @param exception
   * @return
   * @throws Exception
   */
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  @ResponseBody
  public ApiResp<String> validateException(HttpServletRequest request,
                                           MethodArgumentNotValidException exception) throws Exception {
    BindingResult bindingResult = exception.getBindingResult();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    List<String> errorMsgList = new ArrayList<>();
    for (FieldError fieldError : fieldErrors) {
      errorMsgList.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
    }
    return ApiResp.warning(errorMsgList.toString());
  }

  /**
   * 请求内容无法正确解析或读取异常捕获
   * @param request
   * @param exception
   * @return
   * @throws Exception
   */
  @ExceptionHandler(value = HttpMessageNotReadableException.class)
  @ResponseBody
  public ApiResp<String> validateException(HttpServletRequest request,
                                           HttpMessageNotReadableException exception) throws Exception {
    String message = exception.getMessage();
    return ApiResp.warning(message);
  }

  /**
   * 上传文件大小判断的异常捕获
   * @param exception
   * @param response
   * @return
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ApiResp<?> handleMaxSizeException(MaxUploadSizeExceededException exception, HttpServletResponse response) {
    String msg = exception.getMessage();
    return ApiResp.failure("upload file too big, the max size for 10MB, " + msg);
  }

  /**
   * 如果底层抛的是这个也可以一起拦
   * @param exception
   * @return
   */
  @ExceptionHandler(SizeLimitExceededException.class)
  public ApiResp<?> handleSizeLimitExceeded(SizeLimitExceededException exception) {
    String msg = exception.getMessage();
    return ApiResp.failure("request params size is too large, " + msg);
  }

  /**
   * catch BusinessException exception
   * @param exception
   * @return
   */
  @ExceptionHandler(BizException.class)
  public ApiResp<?> handleBusinessException(BizException exception) {
    if (exception.getReturnCodeEnum() != null) {
      return ApiResp.failure(exception.getReturnCodeEnum());
    } else if (exception.getErrorCode() != null) {
      return ApiResp.failure(exception.getErrorCode(), exception.getErrorMsg());
    } else {
      return ApiResp.failure(exception.getMessage());
    }
  }
}