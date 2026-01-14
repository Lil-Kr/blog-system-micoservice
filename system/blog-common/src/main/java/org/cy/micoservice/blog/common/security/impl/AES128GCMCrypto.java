package org.cy.micoservice.blog.common.security.impl;

import com.alibaba.fastjson2.JSONObject;
import org.cy.micoservice.blog.common.security.Crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: 基于AES-128-gcm算法的加解密处理工具
 */
public class AES128GCMCrypto implements Crypto {

  private static final String ALGORITHM = "AES";
  private static final String TRANSFORMATION = "AES/GCM/NoPadding";
  private static final int GCM_TAG_LENGTH = 128; // 认证标签长度(位)
  private static final int GCM_IV_LENGTH = 12;   // 初始向量长度(字节)
  private static final int AES_KEY_SIZE = 128;   // AES-128密钥长度

  private static final String DEFAULT_IV = "9uehHkLsw14g";
  private String key;

  public AES128GCMCrypto(String key) {
    this.key = key;
  }

  @Override
  public String encrypt(String plainText) throws Exception {
    byte[] keyBytes = Base64.getDecoder().decode(key);
    SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

    // 固定IV
    byte[] iv = DEFAULT_IV.getBytes();

    // 初始化加密器
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

    // 执行加密
    byte[] ciphertext = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

    // 合并IV和密文
    byte[] encryptedData = new byte[iv.length + ciphertext.length];
    System.arraycopy(iv, 0, encryptedData, 0, iv.length);
    System.arraycopy(ciphertext, 0, encryptedData, iv.length, ciphertext.length);

    return Base64.getEncoder().encodeToString(encryptedData);
  }

  @Override
  public String decrypt(String cipherText) throws Exception {
    byte[] encryptedData = Base64.getDecoder().decode(cipherText);
    byte[] keyBytes = Base64.getDecoder().decode(key);

    // 分离IV和密文
    byte[] iv = new byte[GCM_IV_LENGTH];
    byte[] ciphertext = new byte[encryptedData.length - GCM_IV_LENGTH];

    System.arraycopy(encryptedData, 0, iv, 0, GCM_IV_LENGTH);
    System.arraycopy(encryptedData, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);

    SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

    // 初始化解密器
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

    // 执行解密
    byte[] decryptedBytes = cipher.doFinal(ciphertext);
    return new String(decryptedBytes, StandardCharsets.UTF_8);
  }

  // 生成AES-128密钥
  private static String generateKeyBase64() throws Exception {
    KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
    keyGen.init(AES_KEY_SIZE);
    SecretKey secretKey = keyGen.generateKey();
    return Base64.getEncoder().encodeToString(secretKey.getEncoded());
  }

  //生成可使用的密钥
  public static void main(String[] args) throws Exception {
    Crypto crypto = new AES128GCMCrypto("PxMNarWuqoNFFGJ5QGgesg==");
    Map<String, Long> map = Map.of("userId", 1330756438846476314L);
    String encryptStr = crypto.encrypt(JSONObject.toJSONString(map));
    System.out.println(encryptStr);
    System.out.println(crypto.decrypt(encryptStr));
  }
}
