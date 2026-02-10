package org.cy.micoservice.blog.common.security;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: 加密处理逻辑
 */
public interface Crypto {

  /**
   * 加密
   *
   * @param plainText
   */
  String encrypt(String plainText) throws Exception;

  /**
   * 解密
   *
   * @param cipherText
   */
  String decrypt(String cipherText) throws Exception;
}