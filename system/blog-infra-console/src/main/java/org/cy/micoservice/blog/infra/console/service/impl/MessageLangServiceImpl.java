package org.cy.micoservice.blog.infra.console.service.impl;

import org.cy.micoservice.blog.infra.console.service.MessageLangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @Author: Lil-K
 * @Date: 2025/3/10
 * @Description: 消息语言服务实现类
 */
@Service
public class MessageLangServiceImpl implements MessageLangService {

	@Autowired
	private MessageSource messageSource;

	/**
	 *
	 * @param lang
	 * @param key
	 * @return
	 */
	@Override
	public String getMessage(String lang, String key) {
		Locale locale = (lang != null && !lang.isBlank()) ? Locale.forLanguageTag(lang) : Locale.getDefault();
		// 兜底，避免 500
		return messageSource.getMessage(key,null, key, locale);
	}


	// @Override
	// public String getMessage(String lang, String key) {
	// 	// Locale locale = lang != null ? new Locale(lang) : Locale.getDefault();
	// 	Locale locale = Locale.forLanguageTag(lang);
	// 	return messageSource.getMessage(key, null, locale);
	//
	// }
}
