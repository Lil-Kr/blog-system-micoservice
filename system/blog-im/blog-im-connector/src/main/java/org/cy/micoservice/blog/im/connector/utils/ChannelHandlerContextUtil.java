package org.cy.micoservice.blog.im.connector.utils;

import io.netty.channel.ChannelHandlerContext;
import org.cy.micoservice.blog.im.connector.config.contstants.ImAttributeKeyConstants;
import org.cy.micoservice.blog.im.facade.connector.enums.ImChannelStatusEnum;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description:
 */
public class ChannelHandlerContextUtil {

  public static void close(ChannelHandlerContext ctx) {
    if (ctx != null && ctx.channel().isActive()) {
      ctx.close();
    }
  }

  public static boolean isLogin(ChannelHandlerContext ctx) {
    if (ctx == null || ! ctx.channel().isActive()) {
      return false;
    }
    Integer code = ContextAttributeUtil.get(ctx, ImAttributeKeyConstants.IDENTIFY_STATUS, Integer.class);
    return code != null && code.equals(ImChannelStatusEnum.HAS_IDENTIFY.getCode());
  }
}