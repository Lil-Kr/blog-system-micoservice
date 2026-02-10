package org.cy.micoservice.app.gateway.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.common.enums.response.RpcReturnCodeEnum;
import org.cy.micoservice.app.common.utils.AssertUtil;
import org.cy.micoservice.app.entity.gateway.model.entity.RouteConfig;
import org.cy.micoservice.app.gateway.facade.enums.GatewayRouterSchemaEnum;
import org.cy.micoservice.app.gateway.service.RouteDefinitionWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory.PATTERN_KEY;

/**
 * @Author: Lil-K
 * @Date: 2025/11/24
 * @Description:
 */
@Slf4j
@Service
public class RouteDefinitionWriterServiceImpl implements RouteDefinitionWriterService {

  private final String ROUTE_CONFIG_PREFIX = "route-config-";

  private final String REFRESH_KEY = "gateway-route-refresh";

  @Autowired
  private RouteDefinitionWriter routeDefinitionWriter;

  @Autowired
  private ApplicationEventPublisher publisher;

  @Override
  public boolean save(RouteConfig routeConfig) {
    try {
      if (!checkRouteConfigValid(routeConfig)) {
        log.error("route config not valid: {}", routeConfig);
        return false;
      }
      RouteDefinition routeDefinition = new RouteDefinition();
      String schema = routeConfig.getSchema();
      AssertUtil.isNotBlank(GatewayRouterSchemaEnum.getByCode(schema), RpcReturnCodeEnum.RPC_PARAMETER_ERROR);

      routeDefinition.setId(ROUTE_CONFIG_PREFIX + routeConfig.getId());
      routeDefinition.setUri(UriComponentsBuilder.fromUriString(routeConfig.getUri()).build().toUri());
      List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
      PredicateDefinition path = new PredicateDefinition();
      path.setName(NameUtils.normalizeRoutePredicateName(PathRoutePredicateFactory.class));
      path.addArg(PATTERN_KEY, routeConfig.getPath());
      predicateDefinitionList.add(path);
      routeDefinition.setPredicates(predicateDefinitionList);

      routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
      // Nacos 2.2.0 需要手动刷新
      publisher.publishEvent(new RefreshRoutesEvent(REFRESH_KEY));
      log.info("route config saved: {}", routeConfig);
      return true;
    } catch (Exception e) {
      log.error("save route config error: {}", e.getMessage());
    }
    return false;
  }

  @Override
  public void delete(Long configId) {
    routeDefinitionWriter.delete(Mono.just(ROUTE_CONFIG_PREFIX + configId)).subscribe();
    publisher.publishEvent(new RefreshRoutesEvent(REFRESH_KEY));
  }

  private boolean checkRouteConfigValid (RouteConfig routeConfig) {
    if (Objects.isNull(routeConfig) || Objects.isNull(routeConfig.getId())) {
      return false;
    }

    if (Objects.isNull(GatewayRouterSchemaEnum.getByCode(routeConfig.getSchema()))) {
      return false;
    }

    if (!routeConfig.getPath().startsWith("/")) {
      return false;
    }
    return true;
  }
}
