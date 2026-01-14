package org.cy.micoservice.blog.im.connector.starter;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.blog.im.connector.config.ImConnectorProperties;
import org.cy.micoservice.blog.im.connector.config.cache.ImChannelCache;
import org.cy.micoservice.blog.im.connector.config.contstants.WebSocketServerConstants;
import org.cy.micoservice.blog.im.connector.config.register.ImConnectorNacosRegister;
import org.cy.micoservice.blog.im.connector.handler.ImMessageHandler;
import org.cy.micoservice.blog.im.connector.handler.WebSocketShakeHandler;
import org.cy.micoservice.blog.im.connector.packet.WebSocketEncoder;
import org.cy.micoservice.blog.im.connector.service.ImMessageSenderService;
import org.cy.micoservice.blog.im.facade.contstants.ImMessageConstants;
import org.cy.micoservice.blog.im.facade.dto.connector.ImMessageDTO;
import org.cy.micoservice.blog.im.facade.dto.connector.body.ImMoveBody;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Lil-K
 * @Date: 2025/12/9
 * @Description:
 */
@Slf4j
@Configuration
public class WebSocketNettyStarter implements InitializingBean {

  private static final int WATER_MARK = 10 * 1024 * 1024;

  @Autowired
  private ImConnectorProperties imConnectorProperties;
  @Autowired
  private WebSocketShakeHandler webSocketShakeHandler;
  @Autowired
  private ImMessageHandler imMessageHandler;
  @Autowired
  private ImChannelCache imChannelCache;
  @Autowired
  private ImConnectorNacosRegister imConnectorNacosRegister;
  @Autowired
  private ImMessageSenderService senderService;

  private NioEventLoopGroup bossGroup;

  private NioEventLoopGroup workerGroup;

  /**
   * todo: 根据操作系统选择实例化对象, mac / windows 下使用 NioEventLoopGroup, Linux下 EpollEventLoopGroup
   * epoll 模型的写法只适用于 Linux 环境下执行, 可以通过环境变量判断操作系统类型, 从而选择初始化哪个写法
   * EpollEventLoopGroup workerGroup = new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
   */
//  private EpollEventLoopGroup bossEpollGroup;
//  private EpollEventLoopGroup workerEpollGroup;

  @Override
  public void afterPropertiesSet() throws Exception {
    Thread startWsServerThread = new Thread(() -> {
      try {
        this.initWebSocketServer();
      } catch (Exception e) {
        log.error("start im server error");
      }
    });

    startWsServerThread.setName(WebSocketServerConstants.WS_SERVER_NAME);
    startWsServerThread.start();
  }

  /**
   * 初始化WebSocket协议的IM服务
   * @throws Exception
   */
  private void initWebSocketServer() throws Exception {
    /**
     * 接收外界IO连接
     */
    AtomicInteger bossThreadId = new AtomicInteger(1);
    bossGroup = new NioEventLoopGroup(1, r -> {
      Thread thread = new Thread(r, WebSocketServerConstants.IM_BOSS_THREAD_PREFIX + bossThreadId.getAndIncrement());
      thread.setDaemon(true);
      return thread;
    });

    /**
     * 负责处理海量连接读写请求事件
     */
    AtomicInteger workerThreadId = new AtomicInteger(1);
    workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, r -> {
      Thread thread = new Thread(r, WebSocketServerConstants.IM_WORKER_THREAD_PREFIX + workerThreadId.getAndIncrement());
      thread.setDaemon(true);
      return thread;
    });

    /**
     * 启动服务
     */
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap
      .group(bossGroup, workerGroup)
      .channel(NioServerSocketChannel.class)
      .childHandler(new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
          /**
           * 处理心跳超时
           */
          channel.config().setKeepAlive(true);
          channel.pipeline().addLast(new IdleStateHandler(3600, 3600, 3600, TimeUnit.SECONDS));

          channel.pipeline().addLast(new HttpServerCodec());
          // 这里面也有一条队列, 用于缓冲对 channelOutBoundBuffer 的压力
          channel.pipeline().addLast(new ChunkedWriteHandler());
          channel.pipeline().addLast(new HttpObjectAggregator(1024 * 10));

          /**
           * 增加自定义的编码器
           */
          channel.pipeline().addLast(new WebSocketEncoder());

          /**
           * im消息的握手处理
           */
          channel.pipeline().addLast(webSocketShakeHandler);

          /**
           * im 消息的业务包处理环节
           */
          channel.pipeline().addLast(imMessageHandler);
        }
    });

    /**
     * 消息最大缓存 --> 10MB
     */
    WriteBufferWaterMark writeBufferWaterMark = new WriteBufferWaterMark(1024 * 1024, WATER_MARK);

    /**
     * option() --> BossGroup
     * childOption() --> WorkGroup
     */
    bootstrap
      .option(ChannelOption.SO_BACKLOG, 3000) // 半连接队列, 该数值决定服务端瞬时连接的吞吐量
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 连接超时5秒
      .option(ChannelOption.SO_REUSEADDR, true) // 允许复用之前的相同端口的资源
      .option(ChannelOption.SO_SNDBUF, WATER_MARK) // 设置TCP缓冲体积大小, 一次拷贝 10M 数据
      // WorkerGroup 相关配置 (已建立连接的 Channel 选项)
      .childOption(ChannelOption.TCP_NODELAY, true) // 禁用 Nagle 算法, 立刻 flush, 避免数据延迟
      // 设置写缓冲区, 最好与 ChannelOption.SO_SNDBUF 保持一致, 不然容易多次从 writeBufferWaterMark 中拷贝数据影响性能
      .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, writeBufferWaterMark)
      .childOption(ChannelOption.SO_KEEPALIVE, true); // 长连接心跳

    /**
     * 优雅关闭, 统一由 ImConnectorShutdownListener 进行管理
     */
    // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    //   bossGroup.shutdownGracefully();
    //   workerGroup.shutdownGracefully();
    // }));

    /**
     * 启动服务
     */
    ChannelFuture channelFuture = bootstrap.bind(imConnectorProperties.getWsPort()).sync();
    log.info("im server start success, port is: {}", imConnectorProperties.getWsPort());
    channelFuture.channel().closeFuture().sync();
  }

  /**
   * Spring boot 优雅关闭时候调用
   */
  public void stopWebSocketServer() {
    try {
      // 修改 Nacos 上, 当前应用实例修改为 unHealth 状态, 并通知
      imConnectorNacosRegister.changeNodeToUnHealth();
      // 检测连接是否已经迁移完成 60s 时限
      for (int i = 0; i < 20; i++) {
        TimeUnit.SECONDS.sleep(3);
        // 最多 20 次重试推送, 如果一直没有断开连接, 则服务端会识别为是僵尸连接
        this.broadcastImMoveMsg(i);
        if (imChannelCache.isEmpty()) {
          // 说明所有连接已经断开了
          break;
        }
      }
      // 超时关闭无用连接, (兜底)
      imChannelCache.closeAllConnAndClearCache();
      // 正式下线
    } catch (Exception e) {
      log.error("stop web socket server error");
    } finally {
      // 删除 nacos 上注册的配置实例
      imConnectorNacosRegister.deregisterTempInstance();
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }

  private void broadcastImMoveMsg(int retryTime) {
    log.info("broadcast im move msg,retryTime: {}", retryTime);
    /**
     * 发送 move 信号给到还在有连接的客户端
     */
    for (Long userId : imChannelCache.getAllChannel().keySet()) {
      ChannelHandlerContext ctx = imChannelCache.get(userId);
      if (ctx.isRemoved() || ! ctx.channel().isActive()) {
        continue;
      }
      ImMoveBody moveBody = new ImMoveBody();
      moveBody.setRetryTimes(retryTime);
      moveBody.setUserId(userId);
      ImMessageDTO moveMsg = new ImMessageDTO(ImMessageConstants.MOVE_CODE, JSONObject.toJSONString(moveBody));
      log.info("im move msg: {}", moveMsg);
      // 通知客户端做重连操作
      senderService.safeWrite(ctx, moveMsg);
    }
  }
}
