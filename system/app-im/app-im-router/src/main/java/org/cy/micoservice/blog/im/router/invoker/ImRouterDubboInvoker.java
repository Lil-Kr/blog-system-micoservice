package org.cy.micoservice.blog.im.router.invoker;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker;
import org.cy.micoservice.blog.im.router.constant.ImRouterConstants;
import org.cy.micoservice.blog.im.router.exception.NotMatchImConnectorException;

import java.util.List;

/**
 * @Author: Lil-K
 * @Date: 2025/12/17
 * @Description: im router invoker, 实现动态获取 im-connector 地址, 并调用 im-connector 中连接的 user_id
 */
public class ImRouterDubboInvoker<T> extends AbstractClusterInvoker<T> {

  public ImRouterDubboInvoker(Directory<T> directory) {
    super(directory);
  }

  /**
   *
   * @param invocation
   * @param invokers
   * @param loadbalance
   * @return
   * @throws RpcException
   */
  @Override
  protected Result doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance) throws RpcException {
    List<Invoker<T>> copyInvokers = invokers;
    checkInvokers(copyInvokers, invocation);

    /**
     * 获取所有调用地址信息, 如果没有任何调用信息, 则直接抛出异常
     */
    String imConnectorAddress = RpcContext.getClientAttachment().getAttachment(ImRouterConstants.IM_ROUTER_DUBBO_CONSTANTS);
    if (StringUtils.isBlank(imConnectorAddress)) {
      // 未找到具体调用机器的地址
      throw new RpcException(new NotMatchImConnectorException("im-connector not find exception im-connector"));
    }

    /**
     * 获取匹配地址的invoker
     */
    List<Invoker<T>> avaliableInvokerList = list(invocation);
    Invoker<T> matchInvoker = avaliableInvokerList.stream()
      .filter(invoker -> imConnectorAddress.equals(invoker.getUrl().getAddress()))
      .findFirst()
      .orElse(null);

    /**
     * 当 matchInvoker == null, 说明下游节点不存在, 无法进行rpc调用
     */
    if (matchInvoker == null) {
      throw new RpcException(new NotMatchImConnectorException("im-connector not match exception im-connector"));
    }
    return invokeWithContext(matchInvoker, invocation);
  }
}
