package org.cy.micoservice.blog.im.gateway.config;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.im.gateway.service.ImConnectorMonitorService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: 获取 im-connector连接负载信息的配置
 */
@Slf4j
@Configuration
public class ImConnectorMonitorConfig implements InitializingBean {

  @Autowired
  private ImGatewayApplicationProperties imGatewayApplicationProperties;
  @Autowired
  private ImConnectorMonitorService imConnectorMonitorService;

  private NamingService namingService;

  /**
   * 从 nacos 中获取
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    try {
      Properties properties = new Properties();
      properties.put("serverAddr", imGatewayApplicationProperties.getNacosAddress());
      properties.put("username", imGatewayApplicationProperties.getNacosUsername());
      properties.put("password", imGatewayApplicationProperties.getNacosPassword());
      properties.put("namespace", imGatewayApplicationProperties.getNacosNamespace());
      namingService = NacosFactory.createNamingService(properties);

      // 2. 注册服务实例变更监听器（核心）
      namingService.subscribe(imGatewayApplicationProperties.getImConnectorClusterName(), event -> {
        // 事件触发时的回调逻辑（实时执行）
        if (event instanceof NamingEvent) {
          Set<String> imConnectorAddressSet = new HashSet<>();
          for (Instance instance : ((NamingEvent) event).getInstances()) {
            if (instance.isHealthy()) {
              imConnectorAddressSet.add(instance.getIp() + ":" + instance.getPort());
            }
          }
          // 存放在本地缓存
          imConnectorMonitorService.refreshCache(imConnectorAddressSet);
          log.info("imConnectorAddressSet:{}", JSONArray.toJSONString(imConnectorAddressSet));
        }
      });
      log.info("subscribe naming event success ,dataId:{}", imGatewayApplicationProperties.getImConnectorClusterName());
    } catch (Exception e) {
      log.error("subscribe naming event error:", e);
    }
  }
}
