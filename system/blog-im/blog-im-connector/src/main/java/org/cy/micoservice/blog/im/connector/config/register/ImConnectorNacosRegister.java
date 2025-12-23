package org.cy.micoservice.blog.im.connector.config.register;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.cy.micoservice.blog.im.connector.config.ImConnectorProperties;
import org.cy.micoservice.blog.im.connector.service.ImMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: im-connector层服务注册到nacos上
 */
@Slf4j
@Component
public class ImConnectorNacosRegister {

  @Autowired
  private ImConnectorProperties imConnectorProperties;
  @Autowired
  private ImMonitorService imMonitorService;

  private NamingService namingService;
  /**
   * Spring初始化完成后，自动注册独立配置到Nacos
   */
  @PostConstruct
  public void registerTempInstance() {
    try {
      // 初始化连接数记录，避免因为之前的连接记录干扰
      imMonitorService.initConnection();

      Properties properties = new Properties();
      properties.put("serverAddr", imConnectorProperties.getNacosAddress());
      properties.put("username", imConnectorProperties.getNacosUsername());
      properties.put("password", imConnectorProperties.getNacosPassword());
      properties.put("namespace", imConnectorProperties.getNacosNamespace());
      namingService = NacosFactory.createNamingService(properties);

      Instance instance = new Instance();
      instance.setIp(NetUtils.getLocalAddress().getHostAddress());
      instance.setPort(imConnectorProperties.getWsPort());
      instance.setWeight(10); // 权重
      instance.setHealthy(true);
      instance.getMetadata().put("createTime", String.valueOf(System.currentTimeMillis()));

      // 注册临时实例（默认ephemeral=true，无需手动设）
      namingService.registerInstance(imConnectorProperties.getImConnectorClusterName(), instance);

      log.info("registerTempInstance success → serviceName:{}", imConnectorProperties.getImConnectorClusterName());
    } catch (Exception e) {
      log.error("registerTempInstance error:", e);
    }
  }
}