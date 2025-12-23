package org.cy.micoservice.blog.gateway.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.entity.gateway.model.enums.GatewayRouterChangeEventEnum;
import org.cy.micoservice.blog.entity.gateway.model.enums.GatewayRouterStatusEnum;
import org.cy.micoservice.blog.gateway.facade.utils.NacosRouteVersionUtils;
import org.cy.micoservice.blog.gateway.service.RouteCacheService;
import org.cy.micoservice.blog.gateway.service.RouteConfigChangeLogService;
import org.cy.micoservice.blog.gateway.service.RouteConfigService;
import org.cy.micoservice.blog.gateway.service.RouteDefinitionWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description: 网关理由配置刷新监听
 */
@Slf4j
@Configuration
public class RouteConfigRefreshListener {

  @Value("${spring.cloud.nacos.discovery.server-addr:}")
  private String serverAddr;

  @Value("${spring.cloud.nacos.discovery.namespace:}")
  private String namespace;

  @Value("${spring.cloud.nacos.username:}")
  private String username;

  @Value("${spring.cloud.nacos.password:}")
  private String password;

  @Value("${blog.gateway.refresh.data-id:}")
  private String refreshDataId;

  @Value("${blog.gateway.refresh.group:}")
  private String refreshGroup;

  private final Long NACOS_TIMEOUT_MS = 10000l;

  private Long currentVersion = 0l;

  @Autowired
  private RouteDefinitionWriterService routeDefinitionWriterService;

  @Autowired
  private RouteConfigService routerConfigService;

  @Autowired
  private RouteConfigChangeLogService routeConfigChangeLogService;

  @Autowired
  private RouteCacheService routeCacheService;

  /**
   * 绑定注册器
   */
  @PostConstruct
  public void registryRouteConfigRefreshListener() throws NacosException {
    Properties properties = new Properties();
    properties.put("serverAddr", serverAddr);
    properties.put("namespace", namespace);
    properties.put("username", username);
    properties.put("password", password);
    ConfigService configService = NacosFactory.createConfigService(properties);
    String dataId = refreshDataId;
    String group = refreshGroup;
    String config = configService.getConfig(dataId, group, NACOS_TIMEOUT_MS);
    log.info("first load config is : {}", config);

    Long version = NacosRouteVersionUtils.parseConfigVersionFromConfig(config);
    this.currentVersion = version;
    log.info("first load config version is : {}", version);

    /**
     * 订阅 Nacos
     */
    configService.addListener(dataId, group, new Listener() {
      @Override
      public Executor getExecutor() {
        // 暂时为空
        return null;
      }

      @Override
      public void receiveConfigInfo(String configInfo) {
        log.info("receive config info: {}", configInfo);
        Long version = NacosRouteVersionUtils.parseConfigVersionFromConfig(configInfo);
        if (version > currentVersion) {
          log.info("current config version is: {}", currentVersion);

          // reload from mysql config then refresh local gateway config
          List<RouteChangeLog> routeChangeLogList = routeConfigChangeLogService.findGtVersion(currentVersion);
          routeChangeLogHandle(routeChangeLogList);

          currentVersion = version;
        }
      }
    });
  }

  /**
   * 更新Gateway中的路由配置数据
   * @param routeChangeLogList
   */
  private void routeChangeLogHandle(List<RouteChangeLog> routeChangeLogList) {
    Set<Long> saveConfigIds = new HashSet<>();
    Set<Long> deletedConfigIds = new HashSet<>();

    for (RouteChangeLog routeChangeLog : routeChangeLogList) {
      String changeEvent = routeChangeLog.getChangeEvent();
      String changeBody = routeChangeLog.getChangeBody();
      JSONObject changeBodyJsonObj = JSON.parseObject(changeBody);
      if (GatewayRouterChangeEventEnum.UPDATE.getCode().equals(changeEvent)) {
        String afterBody = changeBodyJsonObj.getString("after");
        RouteConfig afterRouteConfig = JSON.parseObject(afterBody, RouteConfig.class);
        Integer status = afterRouteConfig.getStatus();

        if (GatewayRouterStatusEnum.INVALID.getCode().equals(status)) {
          deletedConfigIds.add(routeChangeLog.getConfigId());
          routeCacheService.remove(afterRouteConfig.getMethod(), afterRouteConfig.getPath());
        } else {
          saveConfigIds.add(routeChangeLog.getConfigId());
          routeCacheService.put(afterRouteConfig);
        }

      } else if (GatewayRouterChangeEventEnum.DELETED.getCode().equals(changeEvent)) {
        String beforeBody = changeBodyJsonObj.getString("before");
        RouteConfig beforeRouteConfig = JSON.parseObject(beforeBody, RouteConfig.class);
        deletedConfigIds.add(routeChangeLog.getConfigId());
        routeCacheService.remove(beforeRouteConfig.getMethod(), beforeRouteConfig.getPath());
      }
    }

    if (CollectionUtils.isNotEmpty(saveConfigIds)) {
      List<RouteConfig> needSaveConfigs = routerConfigService.findInConfigIds(saveConfigIds);
      for (RouteConfig needSaveConfig : needSaveConfigs) {
        boolean success = routeDefinitionWriterService.save(needSaveConfig);
        if (!success) {
          log.error("save route definition failed, configId: {}", needSaveConfig.getId());
        }
      }
    }

    if (CollectionUtils.isNotEmpty(deletedConfigIds)) {
      for (Long deletedConfigId : deletedConfigIds) {
        routeDefinitionWriterService.delete(deletedConfigId);
      }
    }
  }
}