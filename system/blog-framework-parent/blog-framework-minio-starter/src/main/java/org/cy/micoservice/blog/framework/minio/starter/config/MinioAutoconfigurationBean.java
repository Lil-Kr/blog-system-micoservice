package org.cy.micoservice.blog.framework.minio.starter.config;


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author: Lil-K
 * @Date: 2025/11/23
 * @Description: minio自动装配bean
 */
@Configuration
public class MinioAutoconfigurationBean {

  @Value("${minio.endpoint:}")
  private String endpoint;

  @Value("${minio.access-key:}")
  private String accessKey;

  @Value("${minio.secret-key:}")
  private String secretKey;

  @Bean
  @ConditionalOnBean(value = {MinioClient.class, RedisTemplate.class})
  public MinioClient minioClient() {
    return MinioClient.builder()
      .endpoint(endpoint)
      .credentials(accessKey, secretKey)
      .build();
  }
}