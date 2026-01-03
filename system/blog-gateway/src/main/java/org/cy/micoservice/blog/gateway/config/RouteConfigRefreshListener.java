package org.cy.micoservice.blog.gateway.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteChangeLog;
import org.cy.micoservice.blog.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.blog.gateway.facade.dto.ChangeBodyDTO;
import org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterChangeEventEnum;
import org.cy.micoservice.blog.gateway.facade.enums.GatewayRouterStatusEnum;
import org.cy.micoservice.blog.gateway.facade.utils.NacosRouteVersionUtils;
import org.cy.micoservice.blog.gateway.service.RouteCacheService;
import org.cy.micoservice.blog.gateway.service.RouteConfigChangeLogService;
import org.cy.micoservice.blog.gateway.service.RouteConfigService;
import org.cy.micoservice.blog.gateway.service.RouteDefinitionWriterService;
import org.springframework.beans.factory.annotation.Autowired;
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

  private static final Long NACOS_TIMEOUT_MS = 10000L;

  private Long currentVersion = 0L;

  @Autowired
  private GatewayApplicationProperties applicationProperties;
  @Autowired
  private RouteDefinitionWriterService routeDefinitionWriterService;
  @Autowired
  private RouteConfigService routerConfigService;
  @Autowired
  private RouteConfigChangeLogService routeConfigChangeLogService;
  @Autowired
  private RouteCacheService routeCacheService;

  /**
   * 绑定注册器, 监听nacos的变化通知
   */
  @PostConstruct
  public void registryRouteConfigRefreshListener() throws NacosException {
    Properties properties = new Properties();
    properties.put("serverAddr", applicationProperties.getServerAddr());
    properties.put("namespace", applicationProperties.getNamespace());
    properties.put("username", applicationProperties.getUsername());
    properties.put("password", applicationProperties.getPassword());
    ConfigService configService = NacosFactory.createConfigService(properties);
    String dataId = applicationProperties.getRefreshDataId();
    String group = applicationProperties.getRefreshGroup();
    String config = configService.getConfig(dataId, group, NACOS_TIMEOUT_MS);
    log.info("first load config is: {}", config);

    Long version = NacosRouteVersionUtils.parseConfigVersionFromConfig(config);
    this.currentVersion = version;
    log.info("first load config version is: {}", version);

    /**
     * 订阅 Nacos
     */
    configService.addListener(dataId, group, new Listener() {
      @Override
      public Executor getExecutor() {
        // 暂时为空, 如果不想用nacos的内置线程池, 可以这里注入一个
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
          routeChangeLogHandler(routeChangeLogList);

          currentVersion = version;
        }
      }
    });
  }

  /**
   * 更新Gateway中的路由配置数据
   * @param routeChangeLogList
   */
  private void routeChangeLogHandler(List<RouteChangeLog> routeChangeLogList) {
    Set<Long> saveConfigIds = new HashSet<>();
    Set<Long> deletedConfigIds = new HashSet<>();

    for (RouteChangeLog routeChangeLog : routeChangeLogList) {
      String changeEvent = routeChangeLog.getChangeEvent();
      String changeBody = routeChangeLog.getChangeBody();
      ChangeBodyDTO changeBodyDTO = JSON.parseObject(changeBody, ChangeBodyDTO.class);
      if (GatewayRouterChangeEventEnum.UPDATE.getCode().equals(changeEvent)) {
        RouteConfig afterRouteConfig = changeBodyDTO.getAfter();
        Integer status = afterRouteConfig.getStatus();

        if (GatewayRouterStatusEnum.INVALID.getCode().equals(status)) {
          deletedConfigIds.add(routeChangeLog.getConfigId());
          routeCacheService.remove(afterRouteConfig.getMethod(), afterRouteConfig.getPath());
        } else {
          saveConfigIds.add(routeChangeLog.getConfigId());
          routeCacheService.put(afterRouteConfig);
        }
      } else if (GatewayRouterChangeEventEnum.DELETED.getCode().equals(changeEvent)) {
        RouteConfig beforeRouteConfig = changeBodyDTO.getBefore();
        deletedConfigIds.add(routeChangeLog.getConfigId());
        routeCacheService.remove(beforeRouteConfig.getMethod(), beforeRouteConfig.getPath());
      }
    }

    if (CollectionUtils.isEmpty(saveConfigIds)) {
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