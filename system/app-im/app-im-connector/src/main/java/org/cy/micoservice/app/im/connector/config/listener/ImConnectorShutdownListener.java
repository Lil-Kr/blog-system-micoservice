package org.cy.micoservice.app.im.connector.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.im.connector.starter.WebSocketNettyStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description: 监听 im 节点下线事件
 */
@Slf4j
@Configuration
public class ImConnectorShutdownListener implements ApplicationListener<ContextClosedEvent> {

  @Autowired
  private WebSocketNettyStarter webSocketNettyStarter;

  @Override
  public void onApplicationEvent(ContextClosedEvent event) {
    long now = System.currentTimeMillis();
    log.info("websocket netty server is closing!!");
    webSocketNettyStarter.shutDownWebSocketServer();
    log.info("websocket netty server is closed!! time cost: {} ms", System.currentTimeMillis() - now);
  }
}
