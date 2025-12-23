package org.cy.micoservice.blog.framework.dubbo.starter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.cy.micoservice.blog.common.base.RpcResponse;
import org.cy.micoservice.blog.common.exception.BizException;

/**
 * @Author: Lil-K
 * @Date: 2025/11/23
 * @Description: dubbo 服务端全局异常捕获类
 */
@Activate(group = CommonConstants.PROVIDER)
public class GlobalDubboExceptionFilter implements Filter {

  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    try {
      Result result = invoker.invoke(invocation);
      // 检查是否有异常
      if (result.hasException() && result.getException() != null) {
        Throwable exception = result.getException();
        boolean isBusinessException = exception instanceof BizException;
        // 创建统一错误响应并封装到 RpcResult 中
        if (result instanceof AppResponse appResponse) {
          appResponse.setValue(buildRpcResponseWithException(exception, isBusinessException));
          appResponse.setException(isBusinessException ? null : exception);
          return appResponse;
        } else if (result instanceof AsyncRpcResult asyncRpcResult) {
          asyncRpcResult.setValue(buildRpcResponseWithException(exception, isBusinessException));
          asyncRpcResult.setException(isBusinessException ? null : exception);
          return asyncRpcResult;
        }
      }
      return result;
    } catch (Exception e) {
      throw e;
    }
  }

  private RpcResponse<Object> buildRpcResponseWithException(Throwable exception, boolean isBizException) {
    if (!isBizException) {
      return null;
    }
    BizException bizEx = (BizException) exception;
    RpcResponse<Object> rpcResponse = new RpcResponse<>();
    rpcResponse.setCode(bizEx.getReturnCodeEnum().getCode());
    rpcResponse.setMsg(bizEx.getReturnCodeEnum().getMessage());
    return rpcResponse;
  }
}