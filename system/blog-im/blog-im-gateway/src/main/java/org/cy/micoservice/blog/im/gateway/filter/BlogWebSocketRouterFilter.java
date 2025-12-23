package org.cy.micoservice.blog.im.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.cy.micoservice.blog.im.gateway.dto.ImConnectorMonitorInfo;
import org.cy.micoservice.blog.im.gateway.service.ImConnectorMonitorService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.WebsocketRoutingFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.Comparator;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @Author: Lil-K
 * @Date: 2025/12/19
 * @Description: ws 转发 filter
 */
@Slf4j
@Component
public class BlogWebSocketRouterFilter extends WebsocketRoutingFilter {

  @Autowired
  private ImConnectorMonitorService imConnectorMonitorService;

  public BlogWebSocketRouterFilter(WebSocketClient webSocketClient, WebSocketService webSocketService, ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider) {
    super(webSocketClient, webSocketService, headersFiltersProvider);
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String targetUrlStr = this.getLessConnectionNodeAddress();
    URI targetUri = URI.create(targetUrlStr);

    // 实现最终的转发处理逻辑
    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, targetUri);
    return super.filter(exchange, chain);
  }

  /**
   * 获取最少连接数节点
   * @return
   */
  private String getLessConnectionNodeAddress() {
    List<ImConnectorMonitorInfo> imConnectorMonitorInfoList = imConnectorMonitorService.getAll();
    if (CollectionUtils.isEmpty(imConnectorMonitorInfoList)) {
      log.warn("No Im connector nodes available");
      throw new RuntimeException("No Im connector nodes available");
    }

    ImConnectorMonitorInfo imConnectorMonitorInfo = imConnectorMonitorInfoList.stream()
      .min(Comparator.comparingInt(ImConnectorMonitorInfo::getConnections))
      .orElseThrow(() -> new IllegalStateException("No available IM connector node"));
    String address = String.format("%s%s:%s", "ws://", imConnectorMonitorInfo.getIp(), imConnectorMonitorInfo.getPort());
    log.info("Selected IM connector node with least connections: {} (connections: {})", address, imConnectorMonitorInfo.getConnections());
    return address;
  }
}
