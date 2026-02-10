package org.cy.micoservice.blog.gateway.service;

import org.apache.dubbo.rpc.service.GenericService;

/**
 * @Author: Lil-K
 * @Date: 2025/11/28
 * @Description:
 */
public interface DubboInvokerService {

  void initConfig();

  /**
   * 获取泛化调用实例
   * @param rpcUri
   * @return
   */
  GenericService get(String rpcUri);

  boolean save(String rpcUri);
}
