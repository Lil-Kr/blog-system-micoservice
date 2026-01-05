package org.cy.micoservice.blog.framework.identiy.starter.uitls;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @Author: Lil-K
 * @Date: 2025/11/29
 * @Description: 身份认证统一 template
 * 密钥通过每个工程的配置传递, 不需要HardCode
 */
public class JWTUtil {

  /**
   * 密钥, 建议使用至少256位长度
   */
  private static final String SECRET_KEY = "8276hkhuy0HUgKJBYq08y12kHUYVBK17gkLUQ24cxqwetsEDDfx";

  /**
   * 过期时间
   */
  private static final long EXPIRATION_TIME = 500 * 1000;

  /**
   * 生成JWT, 如果不需要claims的话
   * @param subject
   * @return
   */
  public static String generateToken(String subject, String secretKey, Long expirationTime) {
    return generateToken(subject, new HashMap<>(), secretKey, expirationTime);
  }

  /**
   * 生成带自定义claims的JWT
   * @param subject
   * @param claims
   * @return
   */
  public static String generateToken(String subject, Map<String, Object> claims, String secretKey, Long expirationTime) {
    return Jwts.builder()
      .setClaims(claims) // 声明特殊标识, 相对固定的
      .setSubject(subject) // 主体信息, 唯一标识
      .setIssuedAt(new Date(System.currentTimeMillis())) // 签发时间
      .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 过期时间
      .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256) // 签名
      .compact();
  }

  /**
   * 验证JWT
   * @param token
   * @return
   */
  public static boolean validateToken(String token, String secretKey) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(getSigningKey(secretKey))
        .build()
        .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 从JWT中获取subject
   * @param token
   * @return
   */
  public static String extractSubject(String token, String secretKey) {
    return extractClaim(token, Claims::getSubject, secretKey);
  }

  /**
   * 从JWT中获取过期时间
   * @param token
   * @return
   */
  public static Date extractExpiration(String token, String secretKey) {
    return extractClaim(token, Claims::getExpiration, secretKey);
  }

  /**
   * 从JWT中获取特定claim
   * @param token
   * @param claimsResolver
   * @param <T>
   * @return
   */
  public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secretKey) {
    final Claims claims = extractAllClaims(token, secretKey);
    return claimsResolver.apply(claims);
  }

  /**
   * 获取所有claims, 这里Claims可以理解为是一组身份标识
   * @param token
   * @return
   */
  public static Claims extractAllClaims(String token, String secretKey) {
    return Jwts.parserBuilder()
      .setSigningKey(getSigningKey(secretKey))
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  /**
   * 检查token是否过期 认证关键
   * @param token
   * @return
   */
  public static boolean isTokenExpired(String token, String secretKey) {
    return extractExpiration(token, secretKey).before(new Date());
  }

  /**
   * 获取加密签名的key
   * @return
   */
  private static Key getSigningKey(String secretKey) {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}