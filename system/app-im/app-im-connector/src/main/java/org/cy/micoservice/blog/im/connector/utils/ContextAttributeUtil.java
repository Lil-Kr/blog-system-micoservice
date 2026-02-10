package org.cy.micoservice.blog.im.connector.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @Author: Lil-K
 * @Date: 2025/12/10
 * @Description: 获取netty的ChannelHandlerContext内部绑定的属性
 */
public class ContextAttributeUtil {

  public static <T> T get(ChannelHandlerContext ctx, AttributeKey<Object> attributeKey, Class<T> clazz) {
    Object value = ctx.attr(attributeKey).get();
    if (value == null) {
      return null;
    }
    return (T) clazz.cast(value);
  }

  public static void set(ChannelHandlerContext ctx, AttributeKey<Object> attributeKey, Object value) {
    ctx.attr(attributeKey).set(value);
  }
}