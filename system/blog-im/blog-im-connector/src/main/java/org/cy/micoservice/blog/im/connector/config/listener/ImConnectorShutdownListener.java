package org.cy.micoservice.blog.im.connector.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.im.connector.starter.WebSocketNettyStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @Author idea
 * @Date Created at 2025/12/7
 * @Description 监听 im 节点下线事件
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
    webSocketNettyStarter.stopWebSocketServer();
    log.info("websocket netty server is closed!! time cost: {} ms", System.currentTimeMillis() - now);
  }
}
