package org.cy.micoservice.blog.framework.cache.starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnClass(RedisTemplate.class)
public class RedisConfig {

//  @Bean
//  public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
//    redisTemplate.setConnectionFactory(redisConnectionFactory);
//    StringRedisSerializer valueSerializer = new StringRedisSerializer();
//    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//    redisTemplate.setKeySerializer(stringRedisSerializer);
//    redisTemplate.setValueSerializer(valueSerializer);
//    redisTemplate.setHashKeySerializer(stringRedisSerializer);
//    redisTemplate.setHashValueSerializer(valueSerializer);
//    redisTemplate.afterPropertiesSet();
//    return redisTemplate;
//  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    StringRedisSerializer valueSerializer = new StringRedisSerializer();
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    redisTemplate.setKeySerializer(stringRedisSerializer);
    redisTemplate.setValueSerializer(valueSerializer);
    redisTemplate.setHashKeySerializer(stringRedisSerializer);
    redisTemplate.setHashValueSerializer(valueSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}