package org.cy.micoservice.blog.im.connector;

import com.alibaba.fastjson.JSONObject;
import org.cy.micoservice.blog.framework.identiy.starter.uitls.JWTUtil;
import org.cy.micoservice.blog.im.facade.router.connector.contstants.ImMessageConstants;
import org.cy.micoservice.blog.im.facade.router.connector.dto.ImMessageDTO;
import org.cy.micoservice.blog.im.facade.router.connector.dto.body.ImLoginBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/12/20
 * @Description:
 */
public class CreateImMessageBody {

  private static ImMessageDTO createLoginMsg(Long userId) {
    //需要和im登录逻辑里面的密钥相同
    String imSecretKey = "PIhqklxMNarWuqoNFFGJ5QGgesg==hkl1UBGlqopaKHKq9123h1";
    //设置长期有效
    String loginToken = JWTUtil.generateToken(String.valueOf(userId), new HashMap<>(), imSecretKey, 1000 * 24 * 3600 * 1000L);
    ImLoginBody imLoginBody = new ImLoginBody();
    imLoginBody.setToken(loginToken);
    ImMessageDTO imMessageDTO = new ImMessageDTO(ImMessageConstants.LOGIN_MSG_CODE, JSONObject.toJSONString(imLoginBody));
    return imMessageDTO;
  }

  private static ImMessageDTO createPingMsg() {
    //需要和im登录逻辑里面的密钥相同
    Map<String, Object> pingBody = new HashMap<>();
    pingBody.put("content", "ping");
    ImMessageDTO imMessageDTO = new ImMessageDTO(ImMessageConstants.HEART_BEAT_MSG_CODE, JSONObject.toJSONString(pingBody));
    return imMessageDTO;
  }

  private static ImMessageDTO createCloseMsg() {
    //需要和im登录逻辑里面的密钥相同
    Map<String, Object> pingBody = new HashMap<>();
    pingBody.put("closeStatus", 1);
    ImMessageDTO imMessageDTO = new ImMessageDTO(ImMessageConstants.LOGOUT_MSG_CODE, JSONObject.toJSONString(pingBody));
    return imMessageDTO;
  }

  private static ImMessageDTO createBizMsg(Long receiverId) {
    //需要和im登录逻辑里面的密钥相同
    Map<String, Object> contentBody = new HashMap<>();
    contentBody.put("type", 1);
    contentBody.put("body", "你好, 小绿书测试发送语句");
    //10byte + 4byte + 4byte + 2byte = 20byte
    Map<String, Object> imChatReqMap = new HashMap<>();
    imChatReqMap.put("receiverId", receiverId);
    imChatReqMap.put("relationId", 20991L);
    imChatReqMap.put("content", contentBody);
    ImMessageDTO imMessageDTO = new ImMessageDTO(ImMessageConstants.BIZ_MSG_CODE, JSONObject.toJSONString(imChatReqMap));
    return imMessageDTO;
  }

  public static void main(String[] args) {
    Long senderId = 109891L;
    Long receiverId = 708913L;

    System.out.println("login msg "+ senderId);
    System.out.println(JSONObject.toJSONString(createLoginMsg(senderId)));
    System.out.println("send msg to " + receiverId);
    System.out.println(JSONObject.toJSONString(createBizMsg(receiverId)));

    System.out.println("===================");

    System.out.println("login msg " + receiverId);
    System.out.println(JSONObject.toJSONString(createLoginMsg(receiverId)));
    System.out.println("send msg to " + senderId);
    System.out.println(JSONObject.toJSONString(createBizMsg(senderId)));

    System.out.println("send ping msg to ");
    System.out.println(JSONObject.toJSONString(createPingMsg()));
    System.out.println("send close msg to ");
    System.out.println(JSONObject.toJSONString(createCloseMsg()));
  }
}