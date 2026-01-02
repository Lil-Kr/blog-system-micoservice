package org.cy.micoservice.blog.user.api.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: 2025/12/31
 * @Description: MinIO的配置对象
 */
@Configuration
public class MinIOConfig {

  @Value("${minio.endpoint:}")
  private String endpoint;

  @Value("${minio.access-key:}")
  private String accessKey;

  @Value("${minio.secret-key:}")
  private String secretKey;

  @Bean
  public MinioClient minioClient() {
    return MinioClient.builder()
      .endpoint(endpoint)
      .credentials(accessKey, secretKey)
      .build();
  }
}