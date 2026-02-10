package org.cy.micoservice.app.im.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.app.im.gateway.dto.ImConnectorMonitor;
import org.cy.micoservice.app.im.gateway.service.ImConnectorMonitorService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.WebsocketRoutingFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.Comparator;
import java.util.List;

import static org.cy.micoservice.app.im.facade.contstants.ImMonitorCacheConstant.IM_CONNECTOR_ADDRESS_KEY;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: IM 网关 自定义转发 im-connector 负载 filter
 */
@Slf4j
@Component
public class ImWebSocketRouterFilter extends WebsocketRoutingFilter {

  @Autowired
  private ImConnectorMonitorService imConnectorMonitorService;

  public ImWebSocketRouterFilter(WebSocketClient webSocketClient, WebSocketService webSocketService, ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider) {
    super(webSocketClient, webSocketService, headersFiltersProvider);
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    // 检查是否是 WebSocket 升级请求
    ServerHttpRequest request = exchange.getRequest();
    String upgradeHeader = request.getHeaders().getUpgrade();

    if (upgradeHeader != null && upgradeHeader.toLowerCase().contains("websocket")) {
      try {
        String downStreamUri = this.getLessConnectionNodeAddress() + request.getPath();
        URI targetUri = URI.create(downStreamUri);

        // 实现最终的转发处理逻辑
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, targetUri);
        log.info("Setting target URI for WebSocket connection: {}", targetUri);
      } catch (Exception e) {
        log.error("Error getting least connection node address: ", e);
        // 如果获取节点失败, 继续使用原始路由
        return chain.filter(exchange);
      }
    }
    return super.filter(exchange, chain);
  }

  /**
   * 获取连接数最少的服务信息
   * @return
   */
  private String getLessConnectionNodeAddress() {
    List<ImConnectorMonitor> imConnectorMonitorList = imConnectorMonitorService.getImConnectorMonitorAllList();
    if (CollectionUtils.isEmpty(imConnectorMonitorList)) {
      log.warn("No Im connector nodes available");
      throw new RuntimeException("No Im connector nodes available");
    }

    ImConnectorMonitor imConnectorMonitor = imConnectorMonitorList.stream()
      .min(Comparator.comparingInt(ImConnectorMonitor::getConnections))
      .orElseThrow(() -> new IllegalStateException("No available IM connector node"));

    String address = String.format(IM_CONNECTOR_ADDRESS_KEY, "ws://", imConnectorMonitor.getIp(), imConnectorMonitor.getPort());
    log.info("Selected IM connector node with least connections: {} (connections: {})", address, imConnectorMonitor.getConnections());
    return address;
  }
}