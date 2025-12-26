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
   * Spring初始化完成后, 自动注册独立配置到 Nacos
   */
  @PostConstruct
  public void registerTempInstance() {
    try {
      /**
       * 每次重启服务, 初始化连接数记录
       * 避免在非容器化部署的情况下ip重复的情况
       */
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

      // 注册临时实例（默认ephemeral=true, 无需手动设置）
      namingService.registerInstance(imConnectorProperties.getImConnectorClusterName(), instance);

      log.info("registerTempInstance success → serviceName:{}", imConnectorProperties.getImConnectorClusterName());
    } catch (Exception e) {
      log.error("registerTempInstance error:", e);
    }
  }

  /**
   * 更新当前节点为 unHealth 状态
   */
  public void changeNodeToUnHealth() {
    if (namingService == null) {
      log.error("nacos namingService is null.");
      return;
    }

    try {
      Instance instance = new Instance();
      instance.setIp(NetUtils.getLocalAddress().getHostAddress());
      instance.setPort(imConnectorProperties.getWsPort());
      // 修改节点健康状态: false
      instance.setHealthy(false);
      namingService.registerInstance(imConnectorProperties.getImConnectorClusterName(), instance);
      log.info("changeNodeToUnHealth success → IP:Port = {} : {}", instance.getIp(), instance.getPort());
    } catch (Exception e) {
      log.error("changeNodeToUnHealth error", e);
    }
  }

  /**
   * 可选：服务停止时主动注销实例（加速下线, 非必须, 心跳超时也会自动删）
   */
  public void deregisterTempInstance() {
    if (namingService != null) {
      try {
        Instance instance = new Instance();
        instance.setIp(NetUtils.getLocalAddress().getHostAddress());
        instance.setPort(imConnectorProperties.getWsPort());
        namingService.deregisterInstance(imConnectorProperties.getImConnectorClusterName(), instance);
        log.info("deregisterTempInstance success → IP:Port = {} : {}", instance.getIp(), instance.getPort());
      } catch (Exception e) {
        log.error("deregisterTempInstance error", e);
      }
    }
  }
}