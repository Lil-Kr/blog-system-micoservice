package org.cy.micoservice.app.framework.mybatis.starter.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import org.cy.micoservice.app.common.security.Crypto;
import org.cy.micoservice.app.common.security.impl.AES128GCMCrypto;
import org.cy.micoservice.app.framework.mybatis.starter.interceptor.EncryptInterceptor;
import org.cy.micoservice.app.framework.mybatis.starter.interceptor.DecryptInterceptor;
import org.cy.micoservice.app.framework.mybatis.starter.interceptor.GlobalSqlInterceptor;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/7/20
 * @Description: 分页插件配置
 */
@Configuration
public class MybatisPlusConfig {

  //算法
  @Value("${mybatis.field.crypto.algorithm:AES}")
  private String algorithm;

  //密钥
  @Value("${mybatis.field.crypto.key:S0C86POGXPuaHLBoZMXI+A==}")
  private String cryptoKey;

//  @Bean
//  public MybatisPlusInterceptor mybatisPlusInterceptor() {
//    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//    return interceptor;
//  }

  @Bean
  public GlobalSqlInterceptor globalSqlInterceptor() {
    return new GlobalSqlInterceptor();
  }

  @Bean
  @ConditionalOnProperty(name = "mybatis.field.crypto.enable", havingValue = "true")
  public Crypto crypto() {
    if ("AES".equalsIgnoreCase(algorithm)) {
      return new AES128GCMCrypto(cryptoKey);
    }
    throw new BeanCreationException("mybatis.field.crypto.algorithm: " + algorithm);
  }

  @Bean
  @ConditionalOnProperty(name = "mybatis.field.crypto.enable", havingValue = "true")
  public ConfigurationCustomizer configurationCustomizer(Crypto crypto) {
    return configuration -> {
      configuration.addInterceptor(new DecryptInterceptor(crypto));
      configuration.addInterceptor(new EncryptInterceptor(crypto));
    };
  }
}