package org.cy.micoservice.app.gateway.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.cy.micoservice.app.gateway.config.GatewayApplicationProperties;
import org.cy.micoservice.app.gateway.service.DubboInvokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Lil-K
 * @Date: 2025/11/28
 * @Description: dubbo 转发调用
 */
@Slf4j
@Service
public class DubboInvokerServiceImpl implements DubboInvokerService {

  @Autowired
  private GatewayApplicationProperties applicationProperties;

  private RegistryConfig registryConfig;

  private ApplicationConfig applicationConfig;

  private Map<String, GenericService> referenceConfigMap = new ConcurrentHashMap<>();

  @Override
  public void initConfig() {
    StringBuilder nacosConfig = new StringBuilder()
      .append("nacos://" + applicationProperties.getServerAddr() + "?")
      .append("namespace=" + applicationProperties.getNamespace() + "&")
      .append("username=" + applicationProperties.getUsername() + "&")
      .append("password=" + applicationProperties.getPassword())
    ;
    registryConfig = new RegistryConfig();
    registryConfig.setAddress(nacosConfig.toString());

    applicationConfig = new ApplicationConfig();
    applicationConfig.setName(applicationProperties.getAppName());
    log.info("Registry address: {}", registryConfig.getAddress());
    log.info("Application name: {}", applicationConfig.getName());
  }

  @Override
  public GenericService get(String rpcUri) {
    return referenceConfigMap.getOrDefault(rpcUri, null);
  }

  @Override
  public boolean save(String rpcUri) {
    if (StringUtils.isBlank(rpcUri)) return false;
    log.info("dubbo uri: {}", rpcUri);

    ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
    reference.setRegistry(registryConfig);
    reference.setInterface(rpcUri);
    // 开启泛化调用
    reference.setGeneric("true");
    // 不检查服务提供者是否存在, 防止启动失败
    reference.setCheck(false);
    reference.setApplication(applicationConfig);

    GenericService genericService = reference.get();
    referenceConfigMap.put(rpcUri, genericService);
    return true;
  }
}