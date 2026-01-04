package org.cy.micoservice.blog.common.base.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cy.micoservice.blog.common.enums.response.RpcReturnCodeEnum;
import org.cy.micoservice.blog.common.exception.BizException;

import java.io.Serial;
import java.io.Serializable;

import static org.cy.micoservice.blog.common.enums.response.ApiReturnCodeEnum.SYSTEM_ERROR;
import static org.cy.micoservice.blog.common.enums.response.RpcReturnCodeEnum.RPC_PARAMETER_ERROR;
import static org.cy.micoservice.blog.common.enums.response.RpcReturnCodeEnum.RPC_SUCCESS;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/6/2
 * @Description: rpc接口请求统一对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {

  @Serial
  private static final long serialVersionUID = 5878254696334397967L;

  //响应code
  private Integer code;
  //响应消息code描述文案
  private String msg;
  //数据内容
  private T data;

  public RpcResponse(int code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  private static <T> RpcResponse<T> create(int code, String msg, T data) {
    return new RpcResponse<>(code, msg, data);
  }

  public static <T> RpcResponse<T> unknowSystemError() {
    return create (SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getMessage(), null);
  }

  public static <T> RpcResponse<T> errorParam() {
    return create (RPC_PARAMETER_ERROR.getCode(), RpcReturnCodeEnum.RPC_PARAMETER_ERROR.getMessage(), null);
  }

  public static <T> RpcResponse<T> success() {
    return create (RPC_SUCCESS.getCode(), RPC_SUCCESS.getMessage(), null);
  }

  public static <T> RpcResponse<T> success(T data) {
    return create (RPC_SUCCESS.getCode(), RPC_SUCCESS.getMessage(), data);
  }

  public boolean isSuccess() {
    RpcResponse<T> rpcResponse = this;
    return rpcResponse != null && RPC_SUCCESS.getCode().equals(rpcResponse.getCode());
  }

  public static <T> void isRespSuccess(RpcResponse<T> rpcResponse) {
    if (! rpcResponse.isSuccess()) {
      throw new BizException(rpcResponse.getCode(), rpcResponse.getMsg());
    }
  }
}
