package org.cy.micoservice.blog.im.connector.packet;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.cy.micoservice.blog.im.facade.router.connector.dto.ImMessageDTO;

/**
 * @Author: Lil-K
 * @Date: 2025/12/9
 * @Description: 解决TCP 粘包、半包问题, 明确定义消息体边界符
 * 出站
 */
//public class WebSocketEncoder extends MessageToMessageEncoder<ImMessageDTO> {
public class WebSocketEncoder extends ChannelOutboundHandlerAdapter {

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    if (msg instanceof ImMessageDTO) {
      ctx.write(new TextWebSocketFrame(JSON.toJSONString(msg)), promise);
      return;
    }
    ctx.write(msg, promise);
  }

//  @Override
//  protected void encode(ChannelHandlerContext ctx, ImMessageDTO dto, List<Object> out) throws Exception {
//    out.add(new TextWebSocketFrame(JSON.toJSONString(dto)));
//  }
}