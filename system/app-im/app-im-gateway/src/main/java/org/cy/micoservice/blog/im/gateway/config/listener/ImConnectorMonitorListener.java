package org.cy.micoservice.blog.im.gateway.config.listener;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.common.constants.CommonFormatConstants;
import org.cy.micoservice.blog.im.gateway.config.ImGatewayApplicationProperties;
import org.cy.micoservice.blog.im.gateway.service.ImConnectorMonitorService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: 监听 im-connector 连接负载信息的配置
 */
@Slf4j
@Configuration
public class ImConnectorMonitorListener implements InitializingBean {

  @Autowired
  private ImGatewayApplicationProperties applicationProperties;
  @Autowired
  private ImConnectorMonitorService imConnectorMonitorService;

  private NamingService namingService;

  /**
   * 订阅 nacos, nacos 主动推送下线的 im-connector 服务节点
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    try {
      Properties properties = new Properties();
      properties.put("serverAddr", applicationProperties.getNacosAddress());
      properties.put("username", applicationProperties.getNacosUsername());
      properties.put("password", applicationProperties.getNacosPassword());
      properties.put("namespace", applicationProperties.getNacosNamespace());
      namingService = NacosFactory.createNamingService(properties);

      // 2. 注册服务实例变更监听器(核心)
      namingService.subscribe(applicationProperties.getImConnectorClusterName(), event -> {
        // 事件触发时的回调逻辑(实时执行)
        if (event instanceof NamingEvent) {
          Set<String> imConnectorAddressSet = ((NamingEvent) event).getInstances().stream()
            // nacos: 过滤掉不健康的实例
            .filter(instance -> instance.isHealthy() && instance.isEnabled())
            .map(instance -> String.format(CommonFormatConstants.COMMENT_FORMAT_COLON_SPLIT, instance.getIp(), instance.getPort())) // 转为 ip:port 格式
            .collect(Collectors.toSet());

          // 将最新的配置放入缓存
          imConnectorMonitorService.refreshCache(imConnectorAddressSet);
          log.info("refresh nacos register naming event: {}", JSONArray.toJSONString(imConnectorAddressSet));
        }
      });
      log.info("subscribe naming event success, dataId: {}", applicationProperties.getImConnectorClusterName());
    } catch (Exception e) {
      log.error("subscribe naming event error: ", e);
    }
  }
}
