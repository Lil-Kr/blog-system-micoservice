package org.cy.micoservice.blog.gateway.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.cy.micoservice.blog.gateway.service.DubboInvokeService;
import org.springframework.beans.factory.annotation.Value;
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
public class DubboInvokeServiceImpl implements DubboInvokeService {

  @Value("${spring.application.name:}")
  private String appName;

  private RegistryConfig registryConfig;

  private ApplicationConfig applicationConfig;

  private Map<String, GenericService> referenceConfigMap = new ConcurrentHashMap<>();

  @Override
  public void initConfig() {
    registryConfig = new RegistryConfig();
    registryConfig.setAddress("nacos://192.168.9.200:8848?namespace=blog-mico-service-dev&username=nacos&password=nacos");

    applicationConfig = new ApplicationConfig();
    applicationConfig.setName(appName);
    log.info("Registry address: {}", registryConfig.getAddress());
    log.info("Application name: {}", applicationConfig.getName());
  }

  @Override
  public GenericService get(String uri) {
    return referenceConfigMap.getOrDefault(uri, null);
  }

  @Override
  public boolean save(String uri) {
    if (StringUtils.isBlank(uri)) return false;
    log.info("dubbo uri: {}", uri);

    ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
    reference.setRegistry(registryConfig);
    reference.setInterface(uri);
    // 开启泛化调用
    reference.setGeneric("true");
    // 不检查服务提供者是否存在, 防止启动失败
    reference.setCheck(false);
    reference.setApplication(applicationConfig);

    GenericService genericService = reference.get();
    referenceConfigMap.put(uri, genericService);
    return true;
  }
}