package org.cy.micoservice.app.gateway;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description: 测试利用nacos客户端订阅配置的变更时间
 */
@Slf4j
public class NacosClientTest {

//  @Test
  public void subscribeConfig() throws NacosException, InterruptedException {
    Properties properties = new Properties();
    properties.put("serverAddr", "192.168.9.200:8848");
    properties.put("namespace", "blog-mico-service-dev");
    properties.put("username", "nacos");
    properties.put("password", "nacos");
    ConfigService configService = NacosFactory.createConfigService(properties);
    String dataId = "blog-gateway-config-update-log.properties";
    String group = "DEFAULT_GROUP";
    String content = configService.getConfig(dataId, group, 5000);
    log.info("content is : {}", content);

    // 订阅Nacos
    configService.addListener(dataId, group, new Listener() {
      @Override
      public Executor getExecutor() {
        return null;
      }

      @Override
      public void receiveConfigInfo(String configInfo) {
        log.info("receive config info: {}", configInfo);
      }
    });
    Thread.currentThread().join();
  }

}
