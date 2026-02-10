package org.cy.micoservice.app.im.connector.config.contstants;

import io.netty.util.AttributeKey;

/**
 * @Author: Lil-K
 * @Date: 2025/12/10
 * @Description:
 */
public class ImAttributeKeyConstants {

  public static AttributeKey<Object> WS_URI = AttributeKey.valueOf("webSocketUri");

  public static AttributeKey<Object> SERVER_HAND_SHAKE_KEY = AttributeKey.valueOf("serverHandshakeKey");

  public static AttributeKey<Object> USER_ID = AttributeKey.valueOf("userId");

  public static AttributeKey<Object> IDENTIFY_STATUS = AttributeKey.valueOf("identifyStatus");

  public static AttributeKey<Object> CHANNEL_ID = AttributeKey.valueOf("channelId");

  public static AttributeKey<Object> SHAKE_HAND_TIME = AttributeKey.valueOf("shakeHandTime");

}