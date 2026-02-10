package org.cy.micoservice.app.infra.console.sdk.config;

import com.alibaba.nacos.api.naming.pojo.Instance;
import feign.Feign;
import org.cy.micoservice.app.common.constants.CommonFormatConstants;
import org.cy.micoservice.app.common.constants.gateway.GatewayInfraConsoleSdkConstants;
import org.cy.micoservice.app.infra.console.sdk.interceptor.InfraConsoleSdkHttpRequestInterceptor;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/10/5
 * @Description: feign客户端工厂
 */
public class FeignClientFactory {

  private final NacosServiceDiscovery nacosDiscovery;

  public FeignClientFactory(NacosServiceDiscovery nacosDiscovery) {
    this.nacosDiscovery = nacosDiscovery;
  }

  /**
   * 创建 Feign 客户端 (动态替换 URL 为 Nacos 发现的实例)
   */
  public <T> T createClient(Class<T> clazz, String serviceName, String serviceGroup, String clientName) throws Exception {
    // 从 Nacos 获取一个实例
    Instance instance = nacosDiscovery.getRandomHealthyInstance(serviceName, serviceGroup);
    String httpUrl = String.format(CommonFormatConstants.COMMENT_FORMAT_COLON_SPLIT, GatewayInfraConsoleSdkConstants.HTTP_URL_PREFIX + instance.getIp(), instance.getPort());
    InfraConsoleSdkHttpRequestInterceptor requestInterceptor = new InfraConsoleSdkHttpRequestInterceptor(serviceName, serviceGroup, clientName, nacosDiscovery);
    return Feign.builder()
      .requestInterceptor(requestInterceptor)
      .encoder(FeignJacksonConfig.feignEncoder())
      .decoder(FeignJacksonConfig.feignDecoder())
      .target(clazz, httpUrl);
  }
}