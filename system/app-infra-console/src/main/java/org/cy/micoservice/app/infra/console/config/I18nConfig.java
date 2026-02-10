package org.cy.micoservice.app.infra.console.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @Author: Lil-K
 * @Date: 2026/1/17
 * @Description:
 */
@Configuration
public class I18nConfig {
  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource ms =
      new ReloadableResourceBundleMessageSource();
    ms.setBasename("classpath:i18n/messages");
    ms.setDefaultEncoding("UTF-8");
    ms.setFallbackToSystemLocale(false);
    return ms;
  }
}
