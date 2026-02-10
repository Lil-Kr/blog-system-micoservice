package org.cy.micoservice.app.framework.cache.starter.config;

import lombok.extern.slf4j.Slf4j;
import org.cy.micoservice.app.framework.cache.starter.service.CacheService;
import org.cy.micoservice.app.framework.cache.starter.service.impl.CacheServiceImpl;
import org.springframework.context.annotation.Bean;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/6/29
 * @Description:
 */
@Slf4j
public class CacheAutoconfigurationBean {

    @Bean
    public CacheService<Object> cacheService() {
        log.info("初始化cacheService");
        return new CacheServiceImpl<Object>();
    }
}
