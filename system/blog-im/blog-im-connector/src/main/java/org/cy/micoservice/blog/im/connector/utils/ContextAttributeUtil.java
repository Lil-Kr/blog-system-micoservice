package org.cy.micoservice.blog.im.connector.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @Author: Lil-K
 * @Date: 2025/12/10
 * @Description: 获取netty的ChannelHandlerContext内部绑定的属性
 */
public class ContextAttributeUtil {

  public static <T> T get(ChannelHandlerContext context, AttributeKey<Object> attributeKey, Class<T> clazz) {
    Object value = context.attr(attributeKey).get();
    if (value == null) {
      return null;
    }
    return (T) clazz.cast(value);
  }

  public static void set(ChannelHandlerContext context, AttributeKey<Object> attributeKey, Object value) {
    context.attr(attributeKey).set(value);
  }
}