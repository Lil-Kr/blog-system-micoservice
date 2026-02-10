package org.cy.micoservice.app.infra.console.service;

import com.alibaba.nacos.api.exception.NacosException;

/**
 * @Author: Lil-K
 * @Date: 2025/11/25
 * @Description:
 */
public interface NacosService {

  Long incrVersion() throws NacosException;
}
