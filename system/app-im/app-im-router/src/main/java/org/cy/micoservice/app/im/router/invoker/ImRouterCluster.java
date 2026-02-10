package org.cy.micoservice.app.im.router.invoker;

import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.rpc.cluster.Directory;

/**
 * @Author: Lil-K
 * @Date: 2025/12/18
 * @Description: dubbo SPI扩展
 * 将自定义 ImRouterDubboInvoker 注入到dubbo上下文中让其生效
 */
public class ImRouterCluster implements Cluster {

  @Override
  public <T> Invoker<T> join(Directory<T> directory, boolean buildFilterChain) throws RpcException {
    return new ImRouterDubboInvoker<>(directory);
  }
}
