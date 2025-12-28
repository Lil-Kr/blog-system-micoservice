package org.cy.micoservice.blog.common.utils;

import org.cy.micoservice.blog.common.base.rpc.RpcResponse;
import org.cy.micoservice.blog.common.exception.BizException;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description:
 */
public class RpcAssertUtil {

  /**
   * RPC响应是否正常
   * @param rpcResponse
   */
  public static void isRespSuccess(RpcResponse rpcResponse) {
    if (! rpcResponse.isSuccess()) {
      throw new BizException(rpcResponse.getCode(), rpcResponse.getMsg());
    }
  }
}