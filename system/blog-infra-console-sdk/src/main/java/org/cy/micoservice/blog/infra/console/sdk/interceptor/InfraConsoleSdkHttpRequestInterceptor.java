package org.cy.micoservice.blog.infra.console.sdk.interceptor;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.common.constants.gateway.GatewayInfraConsoleSdkConstants;
import org.cy.micoservice.blog.infra.console.sdk.config.NacosServiceDiscovery;

/**
 * @Author: Lil-K
 * @Date: 2025/11/27
 * @Description:
 */
@Slf4j
public class InfraConsoleSdkHttpRequestInterceptor implements RequestInterceptor {

  private String serviceName;
  private String clientName;
  private String serviceGroup;
  private NacosServiceDiscovery nacosServiceDiscovery;

  public InfraConsoleSdkHttpRequestInterceptor(String serviceName, String serviceGroup, String clientName, NacosServiceDiscovery nacosServiceDiscovery) {
    this.clientName = clientName;
    this.serviceName = serviceName;
    this.serviceGroup = serviceGroup;
    this.nacosServiceDiscovery = nacosServiceDiscovery;
  }

  @Override
  public void apply(RequestTemplate requestTemplate) {
    String httpUrl = null;
    try {
      Instance instance = nacosServiceDiscovery.getRandomHealthyInstance(serviceName, serviceGroup);
      httpUrl = GatewayInfraConsoleSdkConstants.HTTP_URL_PREFIX + instance.getIp() + ":" + instance.getPort();

    } catch (NacosException e) {
      log.error("getRandomHealthyInstance error", e);
    }
    requestTemplate.header("X-INFRA-CONSOLE-SDK-CLIENT", clientName);
    if (StringUtils.isNotBlank(httpUrl)) {
      requestTemplate.target(httpUrl);
    }
  }
}
