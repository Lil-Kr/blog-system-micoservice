package org.cy.micoservice.blog.gateway.service;

import org.apache.dubbo.rpc.service.GenericService;

/**
 * @Author: Lil-K
 * @Date: 2025/11/28
 * @Description:
 */
public interface DubboInvokeService {

  void initConfig();

  /**
   * 获取泛化调用实例
   * @param uri
   * @return
   */
  GenericService get(String uri);

  boolean save(String uri);
}
