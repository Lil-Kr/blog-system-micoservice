package org.cy.micoservice.blog.im.connector.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cy.micoservice.blog.im.connector.utils.ChannelHandlerContextUtil;
import org.cy.micoservice.blog.im.facade.connector.dto.ImMessageDTO;
import org.cy.micoservice.blog.im.connector.handler.template.ImMessageDispatcher;
import org.cy.micoservice.blog.im.facade.connector.enums.ImMessageCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Lil-K
 * @Date: 2025/12/10
 * @Description: 解析上层传递过来的消息体, 拆解成不同的业务消息包, 交给不同的处理器执行
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class ImMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

  @Autowired
  private ImMessageDispatcher imMessageDispatcher;

  @Override
  protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame frame) throws Exception {
    if (frame == null) {
      log.error("invalid msg reach!");
      return;
    }
    /**
     * 身份认证做法:
     * 通过ws的消息包进行身份认证
     * 在im连接初始化之后, 客户端需要立刻发送一个消息包, 这个消息包携带了用户的身份 token 标识
     * 服务端通过 jwt 技术对 token 标识进行验证, 验证通过, 则允许继续通信, 否则直接关闭连接
     *
     * 消息包结构如何设计?
     * code -> code 不同, 消息体不同 (登录消息 / 等出消息 / 业务消息 / 心跳消息)
     */
    String text = frame.text();

    ImMessageDTO dto = JSON.parseObject(text, ImMessageDTO.class);
    // 允许发送 "    " 空字符串
    if (StringUtils.isEmpty(dto.getBody())) {
      log.error("invalid msg length!");
      return;
    }

    if (dto.getCode() <= 0) {
      log.error("invalid msg code!");
      return;
    }

    ImMessageCodeEnum codeEnum = ImMessageCodeEnum.getByCode(dto.getCode());
    if (codeEnum == null) {
      log.error("invalid msg code!");
      return;
    }

    // 按照不同的code走不同的处理器
    imMessageDispatcher.handle(context, dto);
    // log.info("im-connector receive msg: {}", text);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("ImMessageHandler exception: ", cause);
    ChannelHandlerContextUtil.close(ctx);
  }
}