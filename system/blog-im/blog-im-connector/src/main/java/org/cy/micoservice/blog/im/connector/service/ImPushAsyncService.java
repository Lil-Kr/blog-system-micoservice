package org.cy.micoservice.blog.im.connector.service;

/**
 * @Author: Lil-K
 * @Date: 2025/12/11
 * @Description:
 */
public interface ImPushAsyncService {

  /**
   * 发送业务消息到mq队列中
   * @param body
   * @param dispatchTopic
   */
  boolean sendAsyncBizMessageMQ(byte[] body, String dispatchTopic) throws Exception;

  /**
   * 发送登录消息到mq队列中
   * @param body
   * @param imLoginTopic
   */
  boolean sendAsyncLoginMessageMQ(byte[] body, String imLoginTopic) throws Exception;

  /**
   * 发送登出消息到mq队列中
   * @param body
   * @param imLogoutTopic
   */
  boolean sendAsyncLogoutMessageMQ(byte[] body, String imLogoutTopic) throws Exception;


  /**
   * 发送业务消息到mq队列中
   * @param msg
   * @param topic
   */
  boolean sendAsyncBizMessageMQ(String msg, String topic) throws Exception;

  /**
   * 发送登录消息到mq队列中
   * @param msg
   * @param imLoginTopic
   */
  boolean sendAsyncLoginMessageMQ(String msg, String imLoginTopic) throws Exception;

  /**
   * 发送登出消息到mq队列中
   * @param msg
   * @param imLogoutTopic
   */
  boolean sendAsyncLogoutMessageMQ(String msg, String imLogoutTopic) throws Exception;
}